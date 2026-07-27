package com.ak.devforge.controller;

import com.ak.devforge.dto.response.DashboardResponse;
import com.ak.devforge.service.DashboardService;
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

    // GET /api/dashboard
    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboard());
    }
}