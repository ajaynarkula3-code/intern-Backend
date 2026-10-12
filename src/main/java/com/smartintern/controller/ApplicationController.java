package com.smartintern.controller;

import com.smartintern.dto.ApplicationRequest;
import com.smartintern.dto.ApplicationResponseDto;
import com.smartintern.dto.ApplicationStatusUpdateRequest;
import com.smartintern.model.ApplicationStatus;
import com.smartintern.model.Role;
import com.smartintern.security.RequireRole;
import com.smartintern.security.UserContext;
import com.smartintern.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @RequireRole(Role.STUDENT)
    public ResponseEntity<?> apply(@Valid @RequestBody ApplicationRequest request) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Authentication required"));
        }
        try {
            ApplicationResponseDto response = applicationService.apply(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/my")
    @RequireRole(Role.STUDENT)
    public ResponseEntity<?> getMyApplications() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Authentication required"));
        }
        return ResponseEntity.ok(applicationService.getMyApplications(userId));
    }

    @GetMapping
    @RequireRole({Role.ADMIN, Role.HR})
    public ResponseEntity<List<ApplicationResponseDto>> getAllApplications(
            @RequestParam(required = false) Long internshipId,
            @RequestParam(required = false) ApplicationStatus status) {
        Long userId = UserContext.getUserId();
        Role role = UserContext.getRole();
        return ResponseEntity.ok(applicationService.getAllApplications(internshipId, status, userId, role));
    }

    @PutMapping("/{id}/status")
    @RequireRole({Role.ADMIN, Role.HR})
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        Long userId = UserContext.getUserId();
        Role role = UserContext.getRole();
        try {
            ApplicationResponseDto updated = applicationService.updateStatus(id, request, userId, role);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
