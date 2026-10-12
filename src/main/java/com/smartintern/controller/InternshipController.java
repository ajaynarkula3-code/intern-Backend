package com.smartintern.controller;

import com.smartintern.dto.InternshipDto;
import com.smartintern.model.Role;
import com.smartintern.security.RequireRole;
import com.smartintern.security.UserContext;
import com.smartintern.service.InternshipService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/internships")
public class InternshipController {

    private final InternshipService internshipService;

    public InternshipController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    @GetMapping
    public ResponseEntity<List<InternshipDto>> getAllInternships(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "false") boolean all) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return ResponseEntity.ok(internshipService.searchInternships(keyword));
        }
        return ResponseEntity.ok(internshipService.getAllInternships(!all));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInternshipById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(internshipService.getInternshipById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    @RequireRole({Role.ADMIN, Role.HR})
    public ResponseEntity<?> createInternship(@Valid @RequestBody InternshipDto dto) {
        Long userId = UserContext.getUserId();
        try {
            InternshipDto created = internshipService.createInternship(dto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @RequireRole({Role.ADMIN, Role.HR})
    public ResponseEntity<?> updateInternship(@PathVariable Long id, @Valid @RequestBody InternshipDto dto) {
        Long userId = UserContext.getUserId();
        Role role = UserContext.getRole();
        try {
            InternshipDto updated = internshipService.updateInternship(id, dto, userId, role);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @RequireRole({Role.ADMIN, Role.HR})
    public ResponseEntity<?> deleteInternship(@PathVariable Long id) {
        Long userId = UserContext.getUserId();
        Role role = UserContext.getRole();
        try {
            internshipService.deleteInternship(id, userId, role);
            return ResponseEntity.ok(Map.of("message", "Internship deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
