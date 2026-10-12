package com.smartintern.service;

import com.smartintern.dto.ApplicationRequest;
import com.smartintern.dto.ApplicationResponseDto;
import com.smartintern.dto.ApplicationStatusUpdateRequest;
import com.smartintern.dto.EligibilityResultDto;
import com.smartintern.model.*;
import com.smartintern.repository.ApplicationRepository;
import com.smartintern.repository.InternshipRepository;
import com.smartintern.repository.StudentProfileRepository;
import com.smartintern.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final StudentProfileRepository profileRepository;
    private final EligibilityEngineService eligibilityService;
    private final RecommendationEngineService recommendationService;

    public ApplicationService(ApplicationRepository applicationRepository,
                              UserRepository userRepository,
                              InternshipRepository internshipRepository,
                              StudentProfileRepository profileRepository,
                              EligibilityEngineService eligibilityService,
                              RecommendationEngineService recommendationService) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.profileRepository = profileRepository;
        this.eligibilityService = eligibilityService;
        this.recommendationService = recommendationService;
    }

    @Transactional
    public ApplicationResponseDto apply(Long studentUserId, ApplicationRequest request) {
        User student = userRepository.findById(studentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Student user not found: " + studentUserId));

        Internship internship = internshipRepository.findById(request.getInternshipId())
                .orElseThrow(() -> new IllegalArgumentException("Internship not found: " + request.getInternshipId()));

        if (!internship.isActive()) {
            throw new IllegalArgumentException("This internship is no longer accepting applications.");
        }

        // Prevent duplicate applications
        if (applicationRepository.existsByStudentIdAndInternshipId(studentUserId, internship.getId())) {
            throw new IllegalArgumentException("You have already applied for this internship.");
        }

        Application application = new Application(student, internship, request.getCoverLetter());
        Application saved = applicationRepository.save(application);

        return toDto(saved);
    }

    public List<ApplicationResponseDto> getMyApplications(Long studentUserId) {
        return applicationRepository.findByStudentId(studentUserId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ApplicationResponseDto> getAllApplications(Long internshipId, ApplicationStatus status, Long userId, Role role) {
        List<Application> list;

        if (role == Role.HR) {
            list = applicationRepository.findByInternshipPostedById(userId);
            if (internshipId != null) {
                list = list.stream()
                        .filter(a -> a.getInternship() != null && internshipId.equals(a.getInternship().getId()))
                        .collect(Collectors.toList());
            }
        } else if (internshipId != null) {
            list = applicationRepository.findByInternshipId(internshipId);
        } else {
            list = applicationRepository.findAll();
        }

        if (status != null) {
            list = list.stream().filter(a -> a.getStatus() == status).collect(Collectors.toList());
        }

        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ApplicationResponseDto updateStatus(Long applicationId, ApplicationStatusUpdateRequest request, Long updaterId, Role role) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + applicationId));

        if (role == Role.HR) {
            User postedBy = application.getInternship().getPostedBy();
            if (postedBy != null && !postedBy.getId().equals(updaterId)) {
                throw new IllegalArgumentException("You can only manage applicants for your own internships.");
            }
        }

        application.setStatus(request.getStatus());
        if (request.getAdminNotes() != null) {
            application.setAdminNotes(request.getAdminNotes());
        }
        application.setUpdatedAt(LocalDateTime.now());

        Application updated = applicationRepository.save(application);
        return toDto(updated);
    }

    public ApplicationResponseDto toDto(Application app) {
        ApplicationResponseDto dto = new ApplicationResponseDto();
        dto.setId(app.getId());

        Internship i = app.getInternship();
        if (i != null) {
            dto.setInternshipId(i.getId());
            dto.setInternshipTitle(i.getTitle());
            dto.setCompanyName(i.getCompanyName());
            dto.setCompanyLogo(i.getCompanyLogo());
            dto.setDomain(i.getDomain());
            dto.setLocation(i.getLocation());
            dto.setWorkMode(i.getWorkMode());
            dto.setStipend(i.getStipend());
        }

        User u = app.getStudent();
        if (u != null) {
            dto.setStudentId(u.getId());
            dto.setStudentName(u.getName());
            dto.setStudentEmail(u.getEmail());

            StudentProfile profile = profileRepository.findByUserId(u.getId()).orElse(null);
            if (profile != null) {
                dto.setStudentPhone(profile.getPhone());
                dto.setStudentCollege(profile.getCollege());
                dto.setStudentDegree(profile.getDegree());
                dto.setStudentBranch(profile.getBranch());
                dto.setStudentGraduationYear(profile.getGraduationYear());
                dto.setStudentCgpa(profile.getCgpa());
                dto.setResumeUrl(profile.getResumeUrl());
                if (profile.getSkills() != null) {
                    dto.setStudentSkills(profile.getSkills().stream().map(StudentSkill::getSkillName).collect(Collectors.toList()));
                }

                if (i != null) {
                    EligibilityResultDto eligibility = eligibilityService.evaluate(profile, i);
                    dto.setEligibilityStatus(eligibility.getStatus());
                    dto.setMatchPercentage(recommendationService.calculateRecommendation(profile, i).getMatchPercentage());
                }
            }
        }

        dto.setStatus(app.getStatus());
        dto.setCoverLetter(app.getCoverLetter());
        dto.setAdminNotes(app.getAdminNotes());
        dto.setAppliedDate(app.getAppliedDate());
        dto.setUpdatedAt(app.getUpdatedAt());

        return dto;
    }
}
