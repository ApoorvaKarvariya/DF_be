package com.ak.devforge.controller;

import com.ak.devforge.service.RoadmapService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/roadmap")

public class RoadmapController {

    private final RoadmapService roadmapService;

    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    // POST /api/roadmap/generate
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generate(@RequestBody Map<String, String> request) {
        String company = request.getOrDefault("company", "Google");
        return ResponseEntity.ok(roadmapService.generateRoadmap(company));
    }
}
