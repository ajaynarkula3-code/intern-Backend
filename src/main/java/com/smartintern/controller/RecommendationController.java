package com.smartintern.controller;

import com.smartintern.dto.RecommendationDto;
import com.smartintern.model.Role;
import com.smartintern.security.RequireRole;
import com.smartintern.security.UserContext;
import com.smartintern.service.RecommendationEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationEngineService recommendationService;

    public RecommendationController(RecommendationEngineService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    @RequireRole(Role.STUDENT)
    public ResponseEntity<?> getRecommendations() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Authentication required"));
        }
        List<RecommendationDto> list = recommendationService.getRecommendations(userId);
        return ResponseEntity.ok(list);
    }
}
