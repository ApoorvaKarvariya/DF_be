package com.ak.devforge.controller;

import com.ak.devforge.service.LeetCodeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/leetcode")

public class LeetCodeController {

    private final LeetCodeService leetCodeService;

    public LeetCodeController(LeetCodeService leetCodeService) {
        this.leetCodeService = leetCodeService;
    }

    // GET /api/leetcode/me  → my profile
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMyProfile() {
        return ResponseEntity.ok(leetCodeService.getMyLCProfile());
    }

    // GET /api/leetcode/profile/{username}  → any user
    @GetMapping("/profile/{username}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(leetCodeService.getLCProfile(username));
    }

    // POST /api/leetcode/sync  → sync to DB
    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> sync() {
        return ResponseEntity.ok(leetCodeService.syncMyLCData());
    }
}
