package com.smartintern.dto;

import java.util.HashMap;
import java.util.Map;

public class DashboardStatsDto {

    private long totalStudents;
    private long totalInternships;
    private long totalApplications;
    private long totalSelected;
    private long totalShortlisted;
    private long totalPending;
    private long totalRejected;
    private Map<String, Long> domainCounts = new HashMap<>();

    public DashboardStatsDto() {}

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalInternships() {
        return totalInternships;
    }

    public void setTotalInternships(long totalInternships) {
        this.totalInternships = totalInternships;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getTotalSelected() {
        return totalSelected;
    }

    public void setTotalSelected(long totalSelected) {
        this.totalSelected = totalSelected;
    }

    public long getTotalShortlisted() {
        return totalShortlisted;
    }

    public void setTotalShortlisted(long totalShortlisted) {
        this.totalShortlisted = totalShortlisted;
    }

    public long getTotalPending() {
        return totalPending;
    }

    public void setTotalPending(long totalPending) {
        this.totalPending = totalPending;
    }

    public long getTotalRejected() {
        return totalRejected;
    }

    public void setTotalRejected(long totalRejected) {
        this.totalRejected = totalRejected;
    }

    public Map<String, Long> getDomainCounts() {
        return domainCounts;
    }

    public void setDomainCounts(Map<String, Long> domainCounts) {
        this.domainCounts = domainCounts;
    }
}
