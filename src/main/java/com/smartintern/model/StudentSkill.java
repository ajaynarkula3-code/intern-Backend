package com.smartintern.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "student_skills")
public class StudentSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_profile_id", nullable = false)
    private StudentProfile studentProfile;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(name = "proficiency_level")
    private String proficiencyLevel; // Beginner, Intermediate, Advanced

    public StudentSkill() {}

    public StudentSkill(StudentProfile studentProfile, String skillName, String proficiencyLevel) {
        this.studentProfile = studentProfile;
        this.skillName = skillName;
        this.proficiencyLevel = proficiencyLevel != null ? proficiencyLevel : "Intermediate";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentProfile getStudentProfile() {
        return studentProfile;
    }

    public void setStudentProfile(StudentProfile studentProfile) {
        this.studentProfile = studentProfile;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }
}
