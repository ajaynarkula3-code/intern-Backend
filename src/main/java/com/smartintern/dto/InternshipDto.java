package com.smartintern.dto;

import com.smartintern.model.WorkMode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InternshipDto {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Company name is required")
    private String companyName;

    private String companyLogo;

    @NotBlank(message = "Domain is required")
    private String domain;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Work mode is required")
    private WorkMode workMode;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 month")
    private Integer durationMonths;

    @NotNull(message = "Stipend is required")
    @Min(value = 0, message = "Stipend cannot be negative")
    private Double stipend;

    @NotNull(message = "Openings count is required")
    @Min(value = 1, message = "At least 1 opening is required")
    private Integer openings;

    @NotNull(message = "Deadline is required")
    private LocalDate deadline;

    @NotNull(message = "Minimum CGPA is required")
    private Double minCgpa;

    private String eligibleDegrees;
    private String eligibleBranches;
    private String eligibleGraduationYears;

    public static class RequiredSkillDto {
        private String skillName;
        private boolean mandatory = true;

        public RequiredSkillDto() {}

        public RequiredSkillDto(String skillName, boolean mandatory) {
            this.skillName = skillName;
            this.mandatory = mandatory;
        }

        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }

        public boolean isMandatory() {
            return mandatory;
        }

        public void setMandatory(boolean mandatory) {
            this.mandatory = mandatory;
        }
    }

    private List<RequiredSkillDto> requiredSkills = new ArrayList<>();
    private Long postedById;
    private String postedByName;
    private LocalDateTime createdAt;
    private boolean active = true;
    private int applicantCount;

    public InternshipDto() {}

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

    public List<RequiredSkillDto> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<RequiredSkillDto> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public Long getPostedById() {
        return postedById;
    }

    public void setPostedById(Long postedById) {
        this.postedById = postedById;
    }

    public String getPostedByName() {
        return postedByName;
    }

    public void setPostedByName(String postedByName) {
        this.postedByName = postedByName;
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

    public int getApplicantCount() {
        return applicantCount;
    }

    public void setApplicantCount(int applicantCount) {
        this.applicantCount = applicantCount;
    }
}
