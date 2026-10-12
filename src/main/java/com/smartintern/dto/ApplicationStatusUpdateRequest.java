package com.smartintern.dto;

import com.smartintern.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public class ApplicationStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private ApplicationStatus status;

    private String adminNotes;

    public ApplicationStatusUpdateRequest() {}

    public ApplicationStatusUpdateRequest(ApplicationStatus status, String adminNotes) {
        this.status = status;
        this.adminNotes = adminNotes;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
}
