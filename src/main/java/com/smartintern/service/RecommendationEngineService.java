package com.smartintern.service;

import com.smartintern.dto.EligibilityResultDto;
import com.smartintern.dto.RecommendationDto;
import com.smartintern.model.*;
import com.smartintern.repository.InternshipRepository;
import com.smartintern.repository.StudentProfileRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationEngineService {

    private final StudentProfileRepository profileRepository;
    private final InternshipRepository internshipRepository;
    private final EligibilityEngineService eligibilityService;
    private final InternshipService internshipService;

    public RecommendationEngineService(StudentProfileRepository profileRepository,
                                       InternshipRepository internshipRepository,
                                       EligibilityEngineService eligibilityService,
                                       InternshipService internshipService) {
        this.profileRepository = profileRepository;
        this.internshipRepository = internshipRepository;
        this.eligibilityService = eligibilityService;
        this.internshipService = internshipService;
    }

    public List<RecommendationDto> getRecommendations(Long studentUserId) {
        StudentProfile student = profileRepository.findByUserId(studentUserId)
                .orElse(null);

        List<Internship> internships = internshipRepository.findByActiveTrue();
        List<RecommendationDto> recommendations = new ArrayList<>();

        for (Internship internship : internships) {
            RecommendationDto rec = calculateRecommendation(student, internship);
            recommendations.add(rec);
        }

        // Sort descending by match percentage
        recommendations.sort((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()));

        return recommendations;
    }

    public RecommendationDto calculateRecommendation(StudentProfile student, Internship internship) {
        RecommendationDto dto = new RecommendationDto();
        dto.setInternship(internshipService.toDto(internship));

        if (student == null) {
            dto.setMatchPercentage(50.0);
            dto.setEligibilityStatus("Not Eligible");
            dto.addReason("Complete your student profile to get personalized match scoring.");
            return dto;
        }

        // Evaluate eligibility
        EligibilityResultDto eligibility = eligibilityService.evaluate(student, internship);
        dto.setEligibilityStatus(eligibility.getStatus());

        double totalScore = 0.0;
        List<String> reasons = new ArrayList<>();

        // 1. Skill Match Scoring (Weight: 40 points)
        Set<String> studentSkillSet = student.getSkills() != null
                ? student.getSkills().stream().map(s -> s.getSkillName().toLowerCase().trim()).collect(Collectors.toSet())
                : Collections.emptySet();

        List<RequiredSkill> requiredSkills = internship.getRequiredSkills() != null ? internship.getRequiredSkills() : Collections.emptyList();
        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (RequiredSkill req : requiredSkills) {
            String sName = req.getSkillName().trim();
            if (studentSkillSet.contains(sName.toLowerCase())) {
                matched.add(sName);
            } else {
                missing.add(sName);
            }
        }

        dto.setMatchedSkills(matched);
        dto.setMissingSkills(missing);

        double skillRatio = requiredSkills.isEmpty() ? 1.0 : (double) matched.size() / requiredSkills.size();
        double skillScore = skillRatio * 40.0;
        totalScore += skillScore;
        dto.setSkillMatchPercentage(Math.round(skillRatio * 1000.0) / 10.0);

        if (skillRatio >= 0.8) {
            reasons.add(String.format("High Skill Alignment: Matched %d of %d required skills (%s).",
                    matched.size(), requiredSkills.size(), String.join(", ", matched)));
        } else if (skillRatio >= 0.4) {
            reasons.add(String.format("Moderate Skill Alignment: You possess %d core skills (%s). Bridging %s will enhance your profile.",
                    matched.size(), String.join(", ", matched), String.join(", ", missing)));
        } else {
            reasons.add(String.format("Growth Opportunity: Requires foundational skills in %s.",
                    String.join(", ", missing)));
        }

        // 2. Domain & Interest Match Scoring (Weight: 30 points)
        String preferredDomain = student.getPreferredDomain();
        String internshipDomain = internship.getDomain();
        boolean domainMatched = false;

        if (preferredDomain != null && !preferredDomain.trim().isEmpty() && internshipDomain != null) {
            String pNorm = preferredDomain.toLowerCase().trim();
            String iNorm = internshipDomain.toLowerCase().trim();

            if (pNorm.equals(iNorm) || iNorm.contains(pNorm) || pNorm.contains(iNorm)) {
                totalScore += 30.0;
                domainMatched = true;
                reasons.add(String.format("Direct Domain Match: Internship domain '%s' aligns directly with your preferred interest.", internshipDomain));
            } else if (isRelatedDomain(pNorm, iNorm)) {
                totalScore += 20.0;
                domainMatched = true;
                reasons.add(String.format("Related Domain: '%s' is strongly related to your career interest in '%s'.", internshipDomain, preferredDomain));
            } else {
                totalScore += 8.0;
            }
        } else {
            totalScore += 15.0; // neutral default
        }
        dto.setDomainMatched(domainMatched);

        // 3. Academic & Eligibility Scoring (Weight: 20 points)
        boolean cgpaMatched = false;
        if (student.getCgpa() != null && internship.getMinCgpa() != null) {
            if (student.getCgpa() >= internship.getMinCgpa() + 1.0) {
                totalScore += 12.0;
                cgpaMatched = true;
                reasons.add(String.format("Academic Excellence: Your CGPA (%.2f) significantly exceeds the threshold (%.2f).",
                        student.getCgpa(), internship.getMinCgpa()));
            } else if (student.getCgpa() >= internship.getMinCgpa()) {
                totalScore += 10.0;
                cgpaMatched = true;
                reasons.add(String.format("Eligible Academic Merit: Your CGPA (%.2f) meets the minimum requirement (%.2f).",
                        student.getCgpa(), internship.getMinCgpa()));
            } else if (student.getCgpa() >= internship.getMinCgpa() - 0.3) {
                totalScore += 5.0;
            }
        } else {
            totalScore += 6.0;
        }
        dto.setCgpaMatched(cgpaMatched);

        // Degree/Branch qualification bonus
        if ("Eligible".equals(eligibility.getStatus())) {
            totalScore += 8.0;
        } else if ("Partially Matched".equals(eligibility.getStatus())) {
            totalScore += 4.0;
        }

        // 4. Work Mode / Location Preference (Weight: 10 points)
        if (internship.getWorkMode() == WorkMode.REMOTE) {
            totalScore += 10.0;
            reasons.add("Work Mode: 100% Remote flexibility.");
        } else if (student.getLocationPreference() != null &&
                (internship.getLocation().toLowerCase().contains(student.getLocationPreference().toLowerCase()) ||
                 student.getLocationPreference().equalsIgnoreCase("Remote") ||
                 student.getLocationPreference().equalsIgnoreCase("Any"))) {
            totalScore += 10.0;
            reasons.add(String.format("Location Match: Located in %s which matches your preference.", internship.getLocation()));
        } else {
            totalScore += 4.0;
        }

        // Deduct penalty if hard ineligible
        if ("Not Eligible".equals(eligibility.getStatus())) {
            totalScore = Math.min(totalScore, 45.0); // Cap match score for ineligible roles
        }

        double finalPercentage = Math.min(100.0, Math.max(10.0, Math.round(totalScore * 10.0) / 10.0));
        dto.setMatchPercentage(finalPercentage);
        dto.setReasons(reasons);

        return dto;
    }

    private boolean isRelatedDomain(String d1, String d2) {
        if ((d1.contains("software") || d1.contains("java")) && (d2.contains("software") || d2.contains("java") || d2.contains("web"))) {
            return true;
        }
        if ((d1.contains("ai") || d1.contains("ml") || d1.contains("data")) && (d2.contains("data") || d2.contains("ai") || d2.contains("ml"))) {
            return true;
        }
        if ((d1.contains("cloud") || d1.contains("devops")) && (d2.contains("cloud") || d2.contains("devops") || d2.contains("cybersecurity"))) {
            return true;
        }
        return false;
    }
}
