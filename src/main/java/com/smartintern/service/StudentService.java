package com.smartintern.service;

import com.smartintern.dto.StudentProfileDto;
import com.smartintern.model.Role;
import com.smartintern.model.StudentProfile;
import com.smartintern.model.StudentSkill;
import com.smartintern.model.User;
import com.smartintern.repository.StudentProfileRepository;
import com.smartintern.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentProfileRepository profileRepository;
    private final UserRepository userRepository;

    public StudentService(StudentProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public StudentProfileDto getProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        StudentProfile profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    StudentProfile newProfile = new StudentProfile(user);
                    return profileRepository.save(newProfile);
                });

        return toDto(profile);
    }

    @Transactional
    public StudentProfileDto updateProfile(Long userId, StudentProfileDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            user.setName(dto.getName().trim());
            userRepository.save(user);
        }

        StudentProfile profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> new StudentProfile(user));

        profile.setPhone(dto.getPhone());
        profile.setCollege(dto.getCollege());
        profile.setDegree(dto.getDegree());
        profile.setBranch(dto.getBranch());
        profile.setGraduationYear(dto.getGraduationYear());
        profile.setCgpa(dto.getCgpa());
        profile.setPreferredDomain(dto.getPreferredDomain());
        profile.setLocationPreference(dto.getLocationPreference());
        profile.setBio(dto.getBio());
        profile.setResumeUrl(dto.getResumeUrl());
        profile.setUpdatedAt(LocalDateTime.now());

        // Update skills
        profile.getSkills().clear();
        if (dto.getSkills() != null) {
            for (StudentProfileDto.SkillDto skillDto : dto.getSkills()) {
                if (skillDto.getSkillName() != null && !skillDto.getSkillName().trim().isEmpty()) {
                    StudentSkill skill = new StudentSkill(profile, skillDto.getSkillName().trim(), skillDto.getProficiencyLevel());
                    profile.getSkills().add(skill);
                }
            }
        }

        StudentProfile saved = profileRepository.save(profile);
        return toDto(saved);
    }

    public List<StudentProfileDto> getAllStudents() {
        List<User> students = userRepository.findByRole(Role.STUDENT);
        return students.stream()
                .map(u -> getProfileByUserId(u.getId()))
                .collect(Collectors.toList());
    }

    public StudentProfileDto getStudentById(Long studentId) {
        return getProfileByUserId(studentId);
    }

    public StudentProfileDto toDto(StudentProfile profile) {
        StudentProfileDto dto = new StudentProfileDto();
        dto.setId(profile.getId());
        if (profile.getUser() != null) {
            dto.setUserId(profile.getUser().getId());
            dto.setName(profile.getUser().getName());
            dto.setEmail(profile.getUser().getEmail());
        }
        dto.setPhone(profile.getPhone());
        dto.setCollege(profile.getCollege());
        dto.setDegree(profile.getDegree());
        dto.setBranch(profile.getBranch());
        dto.setGraduationYear(profile.getGraduationYear());
        dto.setCgpa(profile.getCgpa());
        dto.setPreferredDomain(profile.getPreferredDomain());
        dto.setLocationPreference(profile.getLocationPreference());
        dto.setBio(profile.getBio());
        dto.setResumeUrl(profile.getResumeUrl());

        if (profile.getSkills() != null) {
            List<StudentProfileDto.SkillDto> skillDtos = profile.getSkills().stream()
                    .map(s -> new StudentProfileDto.SkillDto(s.getSkillName(), s.getProficiencyLevel()))
                    .collect(Collectors.toList());
            dto.setSkills(skillDtos);
        } else {
            dto.setSkills(new ArrayList<>());
        }
        return dto;
    }
}
