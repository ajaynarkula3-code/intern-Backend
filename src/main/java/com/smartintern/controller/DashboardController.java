package com.smartintern.controller;

import com.smartintern.dto.DashboardStatsDto;
import com.smartintern.model.Role;
import com.smartintern.security.RequireRole;
import com.smartintern.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    @RequireRole({Role.ADMIN, Role.HR})
    public ResponseEntity<DashboardStatsDto> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }
}
