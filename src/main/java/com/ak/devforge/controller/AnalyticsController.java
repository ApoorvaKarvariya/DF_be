package com.ak.devforge.controller;

import com.ak.devforge.service.AnalyticsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")

public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    // GET /api/analytics/score-trend
    @GetMapping("/score-trend")
    public ResponseEntity<List<Map<String, Object>>> getScoreTrend() {
        return ResponseEntity.ok(analyticsService.getScoreTrend());
    }

    // GET /api/analytics/category-accuracy
    @GetMapping("/category-accuracy")
    public ResponseEntity<List<Map<String, Object>>> getCategoryAccuracy() {
        return ResponseEntity.ok(analyticsService.getCategoryAccuracy());
    }

    // GET /api/analytics/summary
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        return ResponseEntity.ok(analyticsService.getSummary());
    }
}
