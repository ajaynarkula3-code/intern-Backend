package com.smartintern.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "internships")
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_logo")
    private String companyLogo;

    @Column(nullable = false)
    private String domain; // Software Development, Java Development, Web Development, Data Analytics, AI/ML, Cybersecurity, Cloud Computing

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private String location; // Bangalore, Remote, Hyderabad, Pune, etc.

    @Enumerated(EnumType.STRING)
    @Column(name = "work_mode", nullable = false)
    private WorkMode workMode; // REMOTE, ONSITE, HYBRID

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @Column(nullable = false)
    private Double stipend; // Monthly stipend in INR (e.g., 25000)

    @Column(nullable = false)
    private Integer openings;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column(name = "min_cgpa", nullable = false)
    private Double minCgpa;

    @Column(name = "eligible_degrees")
    private String eligibleDegrees; // "B.Tech, B.E., BCA, MCA, M.Tech" or "All"

    @Column(name = "eligible_branches")
    private String eligibleBranches; // "CSE, IT, ECE, Data Science" or "All"

    @Column(name = "eligible_graduation_years")
    private String eligibleGraduationYears; // "2024, 2025, 2026, 2027" or "All"

    @OneToMany(mappedBy = "internship", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RequiredSkill> requiredSkills = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "posted_by_id")
    private User postedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean active = true;

    public Internship() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public WorkMode getWorkMode() {
        return workMode;
    }

    public void setWorkMode(WorkMode workMode) {
        this.workMode = workMode;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public Double getStipend() {
        return stipend;
    }

    public void setStipend(Double stipend) {
        this.stipend = stipend;
    }

    public Integer getOpenings() {
        return openings;
    }

    public void setOpenings(Integer openings) {
        this.openings = openings;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(Double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public String getEligibleDegrees() {
        return eligibleDegrees;
    }

    public void setEligibleDegrees(String eligibleDegrees) {
        this.eligibleDegrees = eligibleDegrees;
    }

    public String getEligibleBranches() {
        return eligibleBranches;
    }

    public void setEligibleBranches(String eligibleBranches) {
        this.eligibleBranches = eligibleBranches;
    }

    public String getEligibleGraduationYears() {
        return eligibleGraduationYears;
    }

    public void setEligibleGraduationYears(String eligibleGraduationYears) {
        this.eligibleGraduationYears = eligibleGraduationYears;
    }

    public List<RequiredSkill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<RequiredSkill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public void addRequiredSkill(RequiredSkill skill) {
        this.requiredSkills.add(skill);
        skill.setInternship(this);
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
