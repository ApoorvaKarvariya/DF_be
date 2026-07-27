package com.ak.devforge.service;

import com.ak.devforge.model.MockInterview;
import com.ak.devforge.model.User;
import com.ak.devforge.repository.MockInterviewRepository;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class AnalyticsService {

    private final UserService userService;
    private final MockInterviewRepository mockInterviewRepository;

    public AnalyticsService(UserService userService, MockInterviewRepository mockInterviewRepository) {
        this.userService = userService;
        this.mockInterviewRepository = mockInterviewRepository;
    }

    // ── Score Trend (last 30 sessions) ────────────────────────────────────────
    public List<Map<String, Object>> getScoreTrend() {
        User user = userService.getCurrentUser();
        List<MockInterview> interviews = mockInterviewRepository
            .findByUserOrderByCreatedAtDesc(user);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd");

        return interviews.stream()
            .filter(i -> i.getStatus() == MockInterview.Status.COMPLETED
                && i.getAiScore() != null
                && i.getCreatedAt() != null)
            .limit(30)
            .sorted(Comparator.comparing(MockInterview::getCreatedAt))
            .map(i -> {
                Map<String, Object> point = new LinkedHashMap<>();
                point.put("date", i.getCreatedAt().format(fmt));
                point.put("score", i.getAiScore());
                point.put("category", i.getCategory().name());
                point.put("difficulty", i.getDifficulty().name());
                return point;
            })
            .collect(Collectors.toList());
    }

    // ── Category Accuracy ─────────────────────────────────────────────────────
    public List<Map<String, Object>> getCategoryAccuracy() {
        User user = userService.getCurrentUser();
        List<MockInterview> interviews = mockInterviewRepository
            .findByUserOrderByCreatedAtDesc(user);

        return interviews.stream()
            .filter(i -> i.getStatus() == MockInterview.Status.COMPLETED
                && i.getAiScore() != null
                && i.getCategory() != null)
            .collect(Collectors.groupingBy(
                i -> i.getCategory().name(),
                Collectors.averagingInt(MockInterview::getAiScore)
            ))
            .entrySet().stream()
            .map(e -> {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("category", e.getKey().replace("_", " "));
                item.put("avgScore", Math.round(e.getValue() * 10.0) / 10.0);
                return item;
            })
            .sorted(Comparator.comparingDouble(m -> -(double) m.get("avgScore")))
            .collect(Collectors.toList());
    }

    // ── Summary Stats ─────────────────────────────────────────────────────────
    public Map<String, Object> getSummary() {
        User user = userService.getCurrentUser();
        List<MockInterview> all = mockInterviewRepository
            .findByUserOrderByCreatedAtDesc(user);

        List<MockInterview> completed = all.stream()
            .filter(i -> i.getStatus() == MockInterview.Status.COMPLETED)
            .collect(Collectors.toList());

        double avgScore = completed.stream()
            .filter(i -> i.getAiScore() != null)
            .mapToInt(MockInterview::getAiScore)
            .average()
            .orElse(0.0);

        int bestScore = completed.stream()
            .filter(i -> i.getAiScore() != null)
            .mapToInt(MockInterview::getAiScore)
            .max()
            .orElse(0);

        // Category breakdown
        Map<String, Long> categoryCount = all.stream()
            .filter(i -> i.getCategory() != null)
            .collect(Collectors.groupingBy(
                i -> i.getCategory().name(),
                Collectors.counting()
            ));

        // Difficulty breakdown
        Map<String, Long> difficultyCount = all.stream()
            .filter(i -> i.getDifficulty() != null)
            .collect(Collectors.groupingBy(
                i -> i.getDifficulty().name(),
                Collectors.counting()
            ));

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalSessions", all.size());
        summary.put("completedSessions", completed.size());
        summary.put("avgScore", Math.round(avgScore * 10.0) / 10.0);
        summary.put("bestScore", bestScore);
        summary.put("categoryBreakdown", categoryCount);
        summary.put("difficultyBreakdown", difficultyCount);
        summary.put("currentStreak", user.getCurrentStreak());
        summary.put("longestStreak", user.getLongestStreak());
        summary.put("readinessScore", user.getReadinessScore());
        return summary;
    }
}
