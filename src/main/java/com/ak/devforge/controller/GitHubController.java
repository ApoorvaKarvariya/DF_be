package com.ak.devforge.controller;

import com.ak.devforge.service.GitHubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    // GET /api/github/me
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMyProfile() {
        return ResponseEntity.ok(gitHubService.getMyGitHubProfile());
    }

    // GET /api/github/profile/{handle}
    @GetMapping("/profile/{handle}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable String handle) {
        return ResponseEntity.ok(gitHubService.getGitHubProfile(handle));
    }

    // POST /api/github/sync
    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> sync() {
        return ResponseEntity.ok(gitHubService.syncMyGitHubData());
    }
}
