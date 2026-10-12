package com.smartintern.service;

import com.smartintern.dto.InternshipDto;
import com.smartintern.model.Internship;
import com.smartintern.model.RequiredSkill;
import com.smartintern.model.Role;
import com.smartintern.model.User;
import com.smartintern.repository.ApplicationRepository;
import com.smartintern.repository.InternshipRepository;
import com.smartintern.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public InternshipService(InternshipRepository internshipRepository,
                             UserRepository userRepository,
                             ApplicationRepository applicationRepository) {
        this.internshipRepository = internshipRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<InternshipDto> getAllInternships(boolean activeOnly) {
        List<Internship> list = activeOnly ? internshipRepository.findByActiveTrue() : internshipRepository.findAll();
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<InternshipDto> searchInternships(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllInternships(true);
        }
        return internshipRepository.searchActiveInternships(keyword.trim())
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public InternshipDto getInternshipById(Long id) {
        Internship internship = internshipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Internship not found: " + id));
        return toDto(internship);
    }

    public List<InternshipDto> getInternshipsByHr(Long hrId) {
        return internshipRepository.findByPostedById(hrId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public InternshipDto createInternship(InternshipDto dto, Long userId) {
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId).orElse(null);
        }

        Internship internship = new Internship();
        internship.setTitle(dto.getTitle());
        internship.setCompanyName(dto.getCompanyName());
        internship.setCompanyLogo(dto.getCompanyLogo());
        internship.setDomain(dto.getDomain());
        internship.setDescription(dto.getDescription());
        internship.setLocation(dto.getLocation());
        internship.setWorkMode(dto.getWorkMode());
        internship.setDurationMonths(dto.getDurationMonths());
        internship.setStipend(dto.getStipend());
        internship.setOpenings(dto.getOpenings());
        internship.setDeadline(dto.getDeadline());
        internship.setMinCgpa(dto.getMinCgpa());
        internship.setEligibleDegrees(dto.getEligibleDegrees());
        internship.setEligibleBranches(dto.getEligibleBranches());
        internship.setEligibleGraduationYears(dto.getEligibleGraduationYears());
        internship.setPostedBy(user);
        internship.setCreatedAt(LocalDateTime.now());
        internship.setActive(true);

        if (dto.getRequiredSkills() != null) {
            for (InternshipDto.RequiredSkillDto skillDto : dto.getRequiredSkills()) {
                if (skillDto.getSkillName() != null && !skillDto.getSkillName().trim().isEmpty()) {
                    RequiredSkill skill = new RequiredSkill(internship, skillDto.getSkillName().trim(), skillDto.isMandatory());
                    internship.getRequiredSkills().add(skill);
                }
            }
        }

        Internship saved = internshipRepository.save(internship);
        return toDto(saved);
    }

    @Transactional
    public InternshipDto updateInternship(Long id, InternshipDto dto, Long userId, Role role) {
        Internship internship = internshipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Internship not found: " + id));

        // Ensure HR can only update their own listings, Admin can update anything
        if (role == Role.HR && internship.getPostedBy() != null && !internship.getPostedBy().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to edit this internship listing");
        }

        internship.setTitle(dto.getTitle());
        internship.setCompanyName(dto.getCompanyName());
        internship.setCompanyLogo(dto.getCompanyLogo());
        internship.setDomain(dto.getDomain());
        internship.setDescription(dto.getDescription());
        internship.setLocation(dto.getLocation());
        internship.setWorkMode(dto.getWorkMode());
        internship.setDurationMonths(dto.getDurationMonths());
        internship.setStipend(dto.getStipend());
        internship.setOpenings(dto.getOpenings());
        internship.setDeadline(dto.getDeadline());
        internship.setMinCgpa(dto.getMinCgpa());
        internship.setEligibleDegrees(dto.getEligibleDegrees());
        internship.setEligibleBranches(dto.getEligibleBranches());
        internship.setEligibleGraduationYears(dto.getEligibleGraduationYears());
        internship.setActive(dto.isActive());

        internship.getRequiredSkills().clear();
        if (dto.getRequiredSkills() != null) {
            for (InternshipDto.RequiredSkillDto skillDto : dto.getRequiredSkills()) {
                if (skillDto.getSkillName() != null && !skillDto.getSkillName().trim().isEmpty()) {
                    RequiredSkill skill = new RequiredSkill(internship, skillDto.getSkillName().trim(), skillDto.isMandatory());
                    internship.getRequiredSkills().add(skill);
                }
            }
        }

        Internship saved = internshipRepository.save(internship);
        return toDto(saved);
    }

    @Transactional
    public void deleteInternship(Long id, Long userId, Role role) {
        Internship internship = internshipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Internship not found: " + id));

        if (role == Role.HR && internship.getPostedBy() != null && !internship.getPostedBy().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to delete this internship");
        }

        // Remove dependent applications first, otherwise the FK constraint blocks deletion
        applicationRepository.deleteByInternshipId(internship.getId());
        internshipRepository.delete(internship);
    }

    public InternshipDto toDto(Internship entity) {
        InternshipDto dto = new InternshipDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCompanyName(entity.getCompanyName());
        dto.setCompanyLogo(entity.getCompanyLogo());
        dto.setDomain(entity.getDomain());
        dto.setDescription(entity.getDescription());
        dto.setLocation(entity.getLocation());
        dto.setWorkMode(entity.getWorkMode());
        dto.setDurationMonths(entity.getDurationMonths());
        dto.setStipend(entity.getStipend());
        dto.setOpenings(entity.getOpenings());
        dto.setDeadline(entity.getDeadline());
        dto.setMinCgpa(entity.getMinCgpa());
        dto.setEligibleDegrees(entity.getEligibleDegrees());
        dto.setEligibleBranches(entity.getEligibleBranches());
        dto.setEligibleGraduationYears(entity.getEligibleGraduationYears());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setActive(entity.isActive());

        if (entity.getPostedBy() != null) {
            dto.setPostedById(entity.getPostedBy().getId());
            dto.setPostedByName(entity.getPostedBy().getName());
        }

        if (entity.getRequiredSkills() != null) {
            List<InternshipDto.RequiredSkillDto> skills = entity.getRequiredSkills().stream()
                    .map(s -> new InternshipDto.RequiredSkillDto(s.getSkillName(), s.isMandatory()))
                    .collect(Collectors.toList());
            dto.setRequiredSkills(skills);
        } else {
            dto.setRequiredSkills(new ArrayList<>());
        }

        dto.setApplicantCount(applicationRepository.findByInternshipId(entity.getId()).size());

        return dto;
    }
}
