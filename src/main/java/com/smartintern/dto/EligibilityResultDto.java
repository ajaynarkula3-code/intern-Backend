package com.smartintern.dto;

import java.util.ArrayList;
import java.util.List;

public class EligibilityResultDto {

    private Long internshipId;
    private String internshipTitle;
    private String companyName;
    private String status; // Eligible, Not Eligible, Partially Matched
    private boolean eligible;
    private boolean partiallyMatched;
    private double eligibilityScore; // 0 to 100
    private List<EligibilityCheckDetail> checkDetails = new ArrayList<>();
    private String summary;

    public EligibilityResultDto() {}

    public Long getInternshipId() {
        return internshipId;
    }

    public void setInternshipId(Long internshipId) {
        this.internshipId = internshipId;
    }

    public String getInternshipTitle() {
        return internshipTitle;
    }

    public void setInternshipTitle(String internshipTitle) {
        this.internshipTitle = internshipTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public boolean isPartiallyMatched() {
        return partiallyMatched;
    }

    public void setPartiallyMatched(boolean partiallyMatched) {
        this.partiallyMatched = partiallyMatched;
    }

    public double getEligibilityScore() {
        return eligibilityScore;
    }

    public void setEligibilityScore(double eligibilityScore) {
        this.eligibilityScore = eligibilityScore;
    }

    public List<EligibilityCheckDetail> getCheckDetails() {
        return checkDetails;
    }

    public void setCheckDetails(List<EligibilityCheckDetail> checkDetails) {
        this.checkDetails = checkDetails;
    }

    public void addDetail(EligibilityCheckDetail detail) {
        this.checkDetails.add(detail);
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
