package com.smartintern.controller;

import com.smartintern.dto.EligibilityResultDto;
import com.smartintern.model.Role;
import com.smartintern.security.RequireRole;
import com.smartintern.security.UserContext;
import com.smartintern.service.EligibilityEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/eligibility")
public class EligibilityController {

    private final EligibilityEngineService eligibilityService;

    public EligibilityController(EligibilityEngineService eligibilityService) {
        this.eligibilityService = eligibilityService;
    }

    @GetMapping("/{internshipId}")
    public ResponseEntity<?> getEligibilityForCurrentStudent(@PathVariable Long internshipId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Authentication required to evaluate eligibility"));
        }
        try {
            EligibilityResultDto result = eligibilityService.evaluateEligibility(userId, internshipId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/check")
    @RequireRole({Role.ADMIN, Role.HR})
    public ResponseEntity<?> checkEligibility(
            @RequestParam Long studentId,
            @RequestParam Long internshipId) {
        try {
            EligibilityResultDto result = eligibilityService.evaluateEligibility(studentId, internshipId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
