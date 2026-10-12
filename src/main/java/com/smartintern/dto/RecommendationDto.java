package com.smartintern.dto;

import java.util.ArrayList;
import java.util.List;

public class RecommendationDto {

    private InternshipDto internship;
    private double matchPercentage;
    private String eligibilityStatus; // Eligible, Partially Matched, Not Eligible
    private List<String> reasons = new ArrayList<>();
    private double skillMatchPercentage;
    private List<String> matchedSkills = new ArrayList<>();
    private List<String> missingSkills = new ArrayList<>();
    private boolean domainMatched;
    private boolean cgpaMatched;

    public RecommendationDto() {}

    public InternshipDto getInternship() {
        return internship;
    }

    public void setInternship(InternshipDto internship) {
        this.internship = internship;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(double matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public String getEligibilityStatus() {
        return eligibilityStatus;
    }

    public void setEligibilityStatus(String eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public void addReason(String reason) {
        this.reasons.add(reason);
    }

    public double getSkillMatchPercentage() {
        return skillMatchPercentage;
    }

    public void setSkillMatchPercentage(double skillMatchPercentage) {
        this.skillMatchPercentage = skillMatchPercentage;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public boolean isDomainMatched() {
        return domainMatched;
    }

    public void setDomainMatched(boolean domainMatched) {
        this.domainMatched = domainMatched;
    }

    public boolean isCgpaMatched() {
        return cgpaMatched;
    }

    public void setCgpaMatched(boolean cgpaMatched) {
        this.cgpaMatched = cgpaMatched;
    }
}
