package com.ak.devforge.service;

import com.ak.devforge.model.User;


import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RoadmapService {

    private final UserService userService;
    private final AiService aiService;

    public RoadmapService(UserService userService, AiService aiService) {
        this.userService = userService;
        this.aiService = aiService;
    }

    // ── Generate Company Roadmap ───────────────────────────────────────────────
    public Map<String, Object> generateRoadmap(String company) {
        User user = userService.getCurrentUser();

        String prompt = String.format("""
            Create a detailed 4-week interview preparation roadmap for %s.
            
            Candidate Profile:
            - LeetCode Rank: %s
            - LeetCode Acceptance Rate: %s%%
            - Weak Topics: Dynamic Programming, System Design
            
            Return ONLY valid JSON in this exact format:
            {
              "company": "%s",
              "weeks": [
                {
                  "week": 1,
                  "title": "Week title",
                  "goals": ["goal1", "goal2"],
                  "topics": ["topic1", "topic2"],
                  "resources": ["resource1", "resource2"],
                  "dailyTime": "2-3 hours"
                }
              ],
              "tips": ["tip1", "tip2", "tip3"]
            }
            """,
            company,
            user.getLeetcodeRank() != null ? user.getLeetcodeRank() : "Beginner",
            user.getLeetcodeAcceptanceRate() != null ? user.getLeetcodeAcceptanceRate() : 0,
            company
        );

        String aiResponse = aiService.generateResumeContent(prompt);

        // Parse JSON from AI response
        try {
            // Clean markdown code blocks if present
            String cleaned = aiResponse
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

            com.fasterxml.jackson.databind.ObjectMapper mapper =
                new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> roadmap = mapper.readValue(cleaned, Map.class);

            return Map.of(
                "success", true,
                "company", company,
                "roadmap", roadmap
            );
        } catch (Exception e) {
            return Map.of(
                "success", false,
                "company", company,
                "rawContent", aiResponse,
                "error", "Could not parse roadmap structure"
            );
        }
    }
}
