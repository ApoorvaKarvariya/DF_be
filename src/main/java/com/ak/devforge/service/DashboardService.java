package com.ak.devforge.service;

import com.ak.devforge.dto.response.DashboardResponse;
import com.ak.devforge.model.MockInterview;
import com.ak.devforge.model.User;
import com.ak.devforge.repository.MockInterviewRepository;
import com.ak.devforge.repository.SavedQuestionRepository;
import com.ak.devforge.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final MockInterviewRepository mockInterviewRepository;
    private final SavedQuestionRepository savedQuestionRepository;
    private final AiService aiService;

    public DashboardService(UserService userService, UserRepository userRepository, MockInterviewRepository mockInterviewRepository, SavedQuestionRepository savedQuestionRepository, AiService aiService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mockInterviewRepository = mockInterviewRepository;
        this.savedQuestionRepository = savedQuestionRepository;
        this.aiService = aiService;
    }

    // ─── Get Full Dashboard ───────────────────────────────────────────────────

    @Transactional
    public DashboardResponse getDashboard() {
        User user = userService.getCurrentUser();

        // Interview stats
        List<MockInterview> allInterviews =
                mockInterviewRepository.findByUserOrderByCreatedAtDesc(user);

        List<MockInterview> completed = allInterviews.stream()
                .filter(i -> i.getStatus() == MockInterview.Status.COMPLETED)
                .toList();

        double avgScore = completed.stream()
                .filter(i -> i.getAiScore() != null)
                .mapToInt(MockInterview::getAiScore)
                .average()
                .orElse(0.0);

        // Saved questions count
        int savedCount = savedQuestionRepository.findByUser(user).size();

        // Calculate readiness score
        int readiness = calculateReadiness(user, avgScore);

        // Update readiness in DB
        user.setReadinessScore(readiness);
        userRepository.save(user);

        // Get weak topics
        List<String> weakTopics = identifyWeakTopics(user, avgScore);

        // AI study plan
        String studyPlan = "";
        try {
            studyPlan = aiService.generateWeaknessAnalysis(
                    weakTopics,
                    user.getLeetcodeRating());
        } catch (Exception e) {

            studyPlan = "Complete more mock interviews to get a personalized study plan!";
        }

        return DashboardResponse.builder()
                .fullName(user.getFullName())
                .username(user.getUsername())
                .targetCompany(user.getTargetCompany())
                .leetcodeRating(user.getLeetcodeRating())
                .leetcodeRank(user.getLeetcodeRank())
                .leetcodeAcceptanceRate(user.getLeetcodeAcceptanceRate())
                .leetcodeStreak(user.getLeetcodeStreak())
                .currentStreak(user.getCurrentStreak())
                .longestStreak(user.getLongestStreak())
                .totalInterviews(allInterviews.size())
                .completedInterviews(completed.size())
                .averageInterviewScore(Math.round(avgScore * 10.0) / 10.0)
                .atsScore(user.getAtsScore())
                .hasResume(user.getResumePath() != null)
                .totalSavedQuestions(savedCount)
                .readinessScore(readiness)
                .readinessLevel(getReadinessLevel(readiness))
                .studyPlan(studyPlan)
                .weakTopics(weakTopics)
                .build();
    }

    // ─── Calculate Readiness Score ────────────────────────────────────────────

    private int calculateReadiness(User user, double avgInterviewScore) {
        int score = 0;

        // LeetCode rating/ranking strength (max 25 points)
        if (user.getLeetcodeRating() != null) {
            if (user.getLeetcodeRating() >= 2000) score += 25;
            else if (user.getLeetcodeRating() >= 1600) score += 20;
            else if (user.getLeetcodeRating() >= 1200) score += 15;
            else score += 8;
        }

        // Interview score (max 35 points)
        if (avgInterviewScore > 0) {
            score += (int) (avgInterviewScore * 0.35);
        }

        // Resume ATS score (max 15 points)
        if (user.getAtsScore() != null) {
            score += (int) (user.getAtsScore() * 0.15);
        }

        // LeetCode acceptance rate (max 10 points)
        if (user.getLeetcodeAcceptanceRate() != null) {
            score += (int) (user.getLeetcodeAcceptanceRate() * 0.10);
        }

        // Streak bonus - best of app streak / LeetCode streak (max 15 points)
        int bestStreak = Math.max(
                user.getCurrentStreak() != null ? user.getCurrentStreak() : 0,
                user.getLeetcodeStreak() != null ? user.getLeetcodeStreak() : 0
        );
        if (bestStreak >= 30) score += 15;
        else if (bestStreak >= 14) score += 11;
        else if (bestStreak >= 7) score += 7;
        else if (bestStreak >= 3) score += 4;

        return Math.min(score, 100);
    }

    // ─── Get Readiness Level ──────────────────────────────────────────────────

    private String getReadinessLevel(int score) {
        if (score >= 80) return "🔥 Interview Ready!";
        else if (score >= 60) return "💪 Almost There!";
        else if (score >= 40) return "📚 Keep Grinding!";
        else return "🌱 Just Getting Started!";
    }

    // ─── Identify Weak Topics ─────────────────────────────────────────────────

    private List<String> identifyWeakTopics(User user, double avgScore) {
        List<String> weak = new ArrayList<>();

        if (user.getLeetcodeRating() == null ||
                user.getLeetcodeAcceptanceRate() == null ||
                user.getLeetcodeAcceptanceRate() < 50) {
            weak.add("Dynamic Programming");
            weak.add("Graph Algorithms");
        }

        if (avgScore < 60) {
            weak.add("System Design");
            weak.add("Problem Solving");
        }

        if (user.getAtsScore() == null || user.getAtsScore() < 70) {
            weak.add("Resume Building");
        }

        if (user.getLeetcodeHandle() == null) {
            weak.add("Competitive Programming");
        }

        if (weak.isEmpty()) {
            weak.add("Advanced Data Structures");
            weak.add("Behavioral Interview");
        }

        return weak;
    }
}