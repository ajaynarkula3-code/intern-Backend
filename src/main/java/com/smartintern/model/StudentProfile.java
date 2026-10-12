package com.smartintern.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String phone;
    private String college;
    private String degree; // B.Tech, BCA, MCA, M.Tech, B.Sc, etc.
    private String branch; // CSE, IT, ECE, Data Science, etc.

    @Column(name = "graduation_year")
    private Integer graduationYear; // 2024, 2025, 2026, 2027

    private Double cgpa; // e.g., 8.5

    @Column(name = "preferred_domain")
    private String preferredDomain; // Web Development, Software Development, AI/ML, Cloud Computing, Cybersecurity, Data Analytics

    @Column(name = "location_preference")
    private String locationPreference; // Remote, Bangalore, Hyderabad, Pune, etc.

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "resume_url")
    private String resumeUrl;

    @OneToMany(mappedBy = "studentProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<StudentSkill> skills = new ArrayList<>();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public StudentProfile() {}

    public StudentProfile(User user) {
        this.user = user;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public String getPreferredDomain() {
        return preferredDomain;
    }

    public void setPreferredDomain(String preferredDomain) {
        this.preferredDomain = preferredDomain;
    }

    public String getLocationPreference() {
        return locationPreference;
    }

    public void setLocationPreference(String locationPreference) {
        this.locationPreference = locationPreference;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public List<StudentSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<StudentSkill> skills) {
        this.skills = skills;
    }

    public void addSkill(StudentSkill skill) {
        this.skills.add(skill);
        skill.setStudentProfile(this);
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
