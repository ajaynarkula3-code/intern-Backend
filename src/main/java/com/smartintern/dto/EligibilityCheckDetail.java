package com.smartintern.dto;

public class EligibilityCheckDetail {

    private String criteria;
    private String status; // SATISFIED, PARTIAL, NOT_SATISFIED
    private String studentValue;
    private String requiredValue;
    private String reason;

    public EligibilityCheckDetail() {}

    public EligibilityCheckDetail(String criteria, String status, String studentValue, String requiredValue, String reason) {
        this.criteria = criteria;
        this.status = status;
        this.studentValue = studentValue;
        this.requiredValue = requiredValue;
        this.reason = reason;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentValue() {
        return studentValue;
    }

    public void setStudentValue(String studentValue) {
        this.studentValue = studentValue;
    }

    public String getRequiredValue() {
        return requiredValue;
    }

    public void setRequiredValue(String requiredValue) {
        this.requiredValue = requiredValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
