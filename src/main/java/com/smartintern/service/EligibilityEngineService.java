package com.smartintern.service;

import com.smartintern.dto.EligibilityCheckDetail;
import com.smartintern.dto.EligibilityResultDto;
import com.smartintern.model.*;
import com.smartintern.repository.InternshipRepository;
import com.smartintern.repository.StudentProfileRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EligibilityEngineService {

    private final StudentProfileRepository profileRepository;
    private final InternshipRepository internshipRepository;

    public EligibilityEngineService(StudentProfileRepository profileRepository,
                                    InternshipRepository internshipRepository) {
        this.profileRepository = profileRepository;
        this.internshipRepository = internshipRepository;
    }

    public EligibilityResultDto evaluateEligibility(Long studentUserId, Long internshipId) {
        StudentProfile studentProfile = profileRepository.findByUserId(studentUserId)
                .orElse(null);

        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new IllegalArgumentException("Internship not found: " + internshipId));

        return evaluate(studentProfile, internship);
    }

    public EligibilityResultDto evaluate(StudentProfile student, Internship internship) {
        EligibilityResultDto result = new EligibilityResultDto();
        result.setInternshipId(internship.getId());
        result.setInternshipTitle(internship.getTitle());
        result.setCompanyName(internship.getCompanyName());

        if (student == null) {
            result.setStatus("Not Eligible");
            result.setEligible(false);
            result.setPartiallyMatched(false);
            result.setEligibilityScore(0.0);
            result.setSummary("Student profile does not exist or has not been completed.");
            result.addDetail(new EligibilityCheckDetail("Profile Status", "NOT_SATISFIED", "Incomplete", "Complete Profile", "Please complete your profile to verify eligibility."));
            return result;
        }

        double score = 0.0;
        int passedCount = 0;
        int totalGates = 6;
        boolean hardFail = false;

        // 1. CGPA Check
        Double studentCgpa = student.getCgpa();
        Double minCgpa = internship.getMinCgpa();
        if (studentCgpa == null) {
            hardFail = true;
            result.addDetail(new EligibilityCheckDetail(
                    "Minimum CGPA",
                    "NOT_SATISFIED",
                    "Not provided",
                    minCgpa != null ? minCgpa.toString() : "N/A",
                    "CGPA has not been filled out in your profile."
            ));
        } else if (minCgpa == null || studentCgpa >= minCgpa) {
            passedCount++;
            score += 25.0;
            result.addDetail(new EligibilityCheckDetail(
                    "Minimum CGPA",
                    "SATISFIED",
                    studentCgpa.toString(),
                    minCgpa != null ? minCgpa.toString() : "None",
                    "CGPA requirement satisfied (Your CGPA: " + studentCgpa + " >= Required: " + (minCgpa != null ? minCgpa : 0.0) + ")"
            ));
        } else if (studentCgpa >= (minCgpa - 0.4)) {
            score += 12.0;
            result.addDetail(new EligibilityCheckDetail(
                    "Minimum CGPA",
                    "PARTIAL",
                    studentCgpa.toString(),
                    minCgpa.toString(),
                    "Borderline CGPA (Your CGPA: " + studentCgpa + " is close to the threshold of " + minCgpa + ")"
            ));
        } else {
            hardFail = true;
            result.addDetail(new EligibilityCheckDetail(
                    "Minimum CGPA",
                    "NOT_SATISFIED",
                    studentCgpa.toString(),
                    minCgpa.toString(),
                    "CGPA requirement not met (Your CGPA: " + studentCgpa + " < Required: " + minCgpa + ")"
            ));
        }

        // 2. Degree Check
        String studentDegree = student.getDegree();
        String eligibleDegrees = internship.getEligibleDegrees();
        boolean degreeMatch = matchesList(studentDegree, eligibleDegrees);
        if (degreeMatch) {
            passedCount++;
            score += 15.0;
            result.addDetail(new EligibilityCheckDetail(
                    "Degree Qualification",
                    "SATISFIED",
                    studentDegree != null ? studentDegree : "General",
                    eligibleDegrees != null && !eligibleDegrees.trim().isEmpty() ? eligibleDegrees : "All",
                    "Degree requirement satisfied (" + (studentDegree != null ? studentDegree : "Open") + " is eligible)"
            ));
        } else {
            hardFail = true;
            result.addDetail(new EligibilityCheckDetail(
                    "Degree Qualification",
                    "NOT_SATISFIED",
                    studentDegree != null ? studentDegree : "Not specified",
                    eligibleDegrees != null ? eligibleDegrees : "Specific degrees required",
                    "Degree mismatch (Your degree: " + studentDegree + " is not among eligible degrees: " + eligibleDegrees + ")"
            ));
        }

        // 3. Branch Check
        String studentBranch = student.getBranch();
        String eligibleBranches = internship.getEligibleBranches();
        boolean branchMatch = matchesList(studentBranch, eligibleBranches);
        if (branchMatch) {
            passedCount++;
            score += 15.0;
            result.addDetail(new EligibilityCheckDetail(
                    "Branch / Specialization",
                    "SATISFIED",
                    studentBranch != null ? studentBranch : "General",
                    eligibleBranches != null && !eligibleBranches.trim().isEmpty() ? eligibleBranches : "All",
                    "Branch requirement satisfied (" + (studentBranch != null ? studentBranch : "Open") + " is accepted)"
            ));
        } else {
            hardFail = true;
            result.addDetail(new EligibilityCheckDetail(
                    "Branch / Specialization",
                    "NOT_SATISFIED",
                    studentBranch != null ? studentBranch : "Not specified",
                    eligibleBranches != null ? eligibleBranches : "Specific branches required",
                    "Branch mismatch (Your branch: " + studentBranch + " is not in eligible branches: " + eligibleBranches + ")"
            ));
        }

        // 4. Graduation Year Check
        Integer studentGradYear = student.getGraduationYear();
        String eligibleGradYears = internship.getEligibleGraduationYears();
        boolean gradYearMatch = matchesYearList(studentGradYear, eligibleGradYears);
        if (gradYearMatch) {
            passedCount++;
            score += 15.0;
            result.addDetail(new EligibilityCheckDetail(
                    "Graduation Batch",
                    "SATISFIED",
                    studentGradYear != null ? studentGradYear.toString() : "General",
                    eligibleGradYears != null && !eligibleGradYears.trim().isEmpty() ? eligibleGradYears : "All",
                    "Graduation batch satisfied (" + (studentGradYear != null ? studentGradYear : "Open") + " is eligible)"
            ));
        } else {
            hardFail = true;
            result.addDetail(new EligibilityCheckDetail(
                    "Graduation Batch",
                    "NOT_SATISFIED",
                    studentGradYear != null ? studentGradYear.toString() : "Not specified",
                    eligibleGradYears != null ? eligibleGradYears : "Specific batches only",
                    "Graduation batch mismatch (Class of " + studentGradYear + " is not in eligible batches: " + eligibleGradYears + ")"
            ));
        }

        // 5. Skills Check
        Set<String> studentSkillSet = student.getSkills() != null
                ? student.getSkills().stream().map(s -> s.getSkillName().toLowerCase().trim()).collect(Collectors.toSet())
                : Collections.emptySet();

        List<RequiredSkill> requiredSkills = internship.getRequiredSkills() != null ? internship.getRequiredSkills() : Collections.emptyList();
        List<String> matchedSkills = new ArrayList<>();
        List<String> missingMandatory = new ArrayList<>();
        List<String> missingOptional = new ArrayList<>();

        for (RequiredSkill req : requiredSkills) {
            String skillName = req.getSkillName().trim();
            if (studentSkillSet.contains(skillName.toLowerCase())) {
                matchedSkills.add(skillName);
            } else {
                if (req.isMandatory()) {
                    missingMandatory.add(skillName);
                } else {
                    missingOptional.add(skillName);
                }
            }
        }

        if (requiredSkills.isEmpty() || missingMandatory.isEmpty() && missingOptional.isEmpty()) {
            passedCount++;
            score += 20.0;
            result.addDetail(new EligibilityCheckDetail(
                    "Required Skills",
                    "SATISFIED",
                    matchedSkills.toString(),
                    requiredSkills.stream().map(RequiredSkill::getSkillName).collect(Collectors.toList()).toString(),
                    "All required skills matched: " + matchedSkills
            ));
        } else if (missingMandatory.isEmpty()) {
            // All mandatory present, some optional missing
            passedCount++;
            score += 16.0;
            result.addDetail(new EligibilityCheckDetail(
                    "Required Skills",
                    "SATISFIED",
                    matchedSkills.toString(),
                    requiredSkills.stream().map(RequiredSkill::getSkillName).collect(Collectors.toList()).toString(),
                    "All mandatory skills matched (" + matchedSkills + "). Optional missing: " + missingOptional
            ));
        } else {
            // Some mandatory skills missing
            double matchRatio = requiredSkills.size() > 0 ? (double) matchedSkills.size() / requiredSkills.size() : 0.0;
            score += (matchRatio * 20.0);
            if (matchRatio >= 0.5) {
                result.addDetail(new EligibilityCheckDetail(
                        "Required Skills",
                        "PARTIAL",
                        matchedSkills.toString(),
                        requiredSkills.stream().map(RequiredSkill::getSkillName).collect(Collectors.toList()).toString(),
                        "Partially matched skills (" + matchedSkills.size() + "/" + requiredSkills.size() + " matched). Missing mandatory: " + missingMandatory
                ));
            } else {
                result.addDetail(new EligibilityCheckDetail(
                        "Required Skills",
                        "NOT_SATISFIED",
                        matchedSkills.toString(),
                        requiredSkills.stream().map(RequiredSkill::getSkillName).collect(Collectors.toList()).toString(),
                        "Missing critical required skills: " + missingMandatory
                ));
            }
        }

        // 6. Location & Work Mode
        String studentLoc = student.getLocationPreference();
        WorkMode workMode = internship.getWorkMode();
        if (workMode == WorkMode.REMOTE) {
            passedCount++;
            score += 10.0;
            result.addDetail(new EligibilityCheckDetail(
                    "Work Mode & Location",
                    "SATISFIED",
                    studentLoc != null ? studentLoc : "Any",
                    "Remote",
                    "Remote internship - eligible regardless of physical location"
            ));
        } else if (studentLoc == null || studentLoc.equalsIgnoreCase("Remote") || studentLoc.equalsIgnoreCase("Any")
                || containsIgnoreCase(internship.getLocation(), studentLoc) || containsIgnoreCase(studentLoc, internship.getLocation())) {
            passedCount++;
            score += 10.0;
            result.addDetail(new EligibilityCheckDetail(
                    "Work Mode & Location",
                    "SATISFIED",
                    studentLoc != null ? studentLoc : "Flexible",
                    internship.getLocation() + " (" + workMode + ")",
                    "Location preference matches: " + internship.getLocation()
            ));
        } else {
            score += 5.0;
            result.addDetail(new EligibilityCheckDetail(
                    "Work Mode & Location",
                    "PARTIAL",
                    studentLoc,
                    internship.getLocation() + " (" + workMode + ")",
                    "Location difference (Internship in " + internship.getLocation() + ", your preference: " + studentLoc + " - relocation may be required)"
            ));
        }

        result.setEligibilityScore(Math.min(100.0, Math.round(score * 10.0) / 10.0));

        // Determine final label
        if (hardFail) {
            result.setStatus("Not Eligible");
            result.setEligible(false);
            result.setPartiallyMatched(false);
            result.setSummary("You do not meet one or more primary eligibility criteria (CGPA, Degree, Branch, or Graduation Year).");
        } else if (passedCount == totalGates && missingMandatory.isEmpty()) {
            result.setStatus("Eligible");
            result.setEligible(true);
            result.setPartiallyMatched(false);
            result.setSummary("Congratulations! You satisfy all eligibility criteria for this internship.");
        } else {
            result.setStatus("Partially Matched");
            result.setEligible(false);
            result.setPartiallyMatched(true);
            result.setSummary("You meet the primary academic criteria but have partial skill or location alignment.");
        }

        return result;
    }

    private boolean matchesList(String candidate, String allowedCsv) {
        if (allowedCsv == null || allowedCsv.trim().isEmpty() || allowedCsv.equalsIgnoreCase("All")) {
            return true;
        }
        if (candidate == null || candidate.trim().isEmpty()) {
            return false;
        }
        String[] parts = allowedCsv.split(",");
        String normCandidate = candidate.trim().toLowerCase();
        for (String p : parts) {
            String norm = p.trim().toLowerCase();
            if (norm.equals("all") || norm.equals(normCandidate) || normCandidate.contains(norm) || norm.contains(normCandidate)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesYearList(Integer candidateYear, String allowedCsv) {
        if (allowedCsv == null || allowedCsv.trim().isEmpty() || allowedCsv.equalsIgnoreCase("All")) {
            return true;
        }
        if (candidateYear == null) {
            return false;
        }
        String[] parts = allowedCsv.split(",");
        for (String p : parts) {
            String norm = p.trim().toLowerCase();
            if (norm.equals("all") || norm.equals(candidateYear.toString())) {
                return true;
            }
        }
        return false;
    }

    private boolean containsIgnoreCase(String src, String search) {
        if (src == null || search == null) return false;
        return src.toLowerCase().contains(search.toLowerCase());
    }
}
