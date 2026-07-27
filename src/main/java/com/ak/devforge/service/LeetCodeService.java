package com.ak.devforge.service;

import com.ak.devforge.model.User;
import com.ak.devforge.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class LeetCodeService {

    private final UserRepository userRepository;
    private final UserService userService;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://leetcode.com")
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("Referer", "https://leetcode.com")
            .build();

    public LeetCodeService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // ── Fetch total question counts per difficulty (site-wide, cached) ────────
    private Map<String, Integer> totalCountsCache = null;

    private Map<String, Integer> fetchTotalQuestionCounts() {
        if (totalCountsCache != null) return totalCountsCache;
        String query = """
            {
              "query": "{ allQuestionsCount { difficulty count } }"
            }
            """;
        try {
            Map response = webClient.post()
                    .uri("/graphql")
                    .bodyValue(query)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            Map<String, Integer> result = new java.util.HashMap<>();
            if (response != null && response.containsKey("data")) {
                Map data = (Map) response.get("data");
                List<Map> list = (List<Map>) data.get("allQuestionsCount");
                if (list != null) {
                    for (Map item : list) {
                        String diff = (String) item.get("difficulty");
                        int count = ((Number) item.get("count")).intValue();
                        result.put(diff, count);
                    }
                }
            }
            totalCountsCache = result;
            return result;
        } catch (Exception e) {
            return Map.of();
        }
    }

    // ── GraphQL query to fetch LC profile (stats + acceptance rate + streak) ──
    private Map<String, Object> fetchLCData(String username) {
        String query = """
            {
              "query": "query getUserProfile($username: String!) { matchedUser(username: $username) { profile { ranking } submitStats { acSubmissionNum { difficulty count submissions } totalSubmissionNum { difficulty count submissions } } userCalendar { streak totalActiveDays } } }",
              "variables": { "username": "%s" }
            }
            """.formatted(username);

        try {
            Map response = webClient.post()
                    .uri("/graphql")
                    .bodyValue(query)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("data")) {
                Map data = (Map) response.get("data");
                return (Map) data.get("matchedUser");
            }
        } catch (Exception e) {

        }
        return null;
    }

    // ── Get profile of any LC user ────────────────────────────────────────────
    public Map<String, Object> getLCProfile(String username) {
        Map<String, Object> userData = fetchLCData(username);
        if (userData == null) {
            throw new RuntimeException("LeetCode user not found: " + username);
        }

        Map profile = (Map) userData.get("profile");
        Map submitStats = (Map) userData.get("submitStats");
        Map userCalendar = (Map) userData.get("userCalendar");

        int ranking = profile != null && profile.get("ranking") != null
                ? ((Number) profile.get("ranking")).intValue() : 0;

        int easySolved = 0, mediumSolved = 0, hardSolved = 0, totalAccepted = 0;
        int totalSubmissions = 0;

        if (submitStats != null) {
            List<Map> acList = (List<Map>) submitStats.get("acSubmissionNum");
            if (acList != null) {
                for (Map item : acList) {
                    String diff = (String) item.get("difficulty");
                    int count = ((Number) item.get("count")).intValue();
                    if ("Easy".equals(diff)) easySolved = count;
                    else if ("Medium".equals(diff)) mediumSolved = count;
                    else if ("Hard".equals(diff)) hardSolved = count;
                    else if ("All".equals(diff)) totalAccepted = count;
                }
            }

            List<Map> totalList = (List<Map>) submitStats.get("totalSubmissionNum");
            if (totalList != null) {
                for (Map item : totalList) {
                    if ("All".equals(item.get("difficulty"))) {
                        totalSubmissions = ((Number) item.get("count")).intValue();
                    }
                }
            }
        }

        int acceptanceRate = totalSubmissions > 0
                ? (int) Math.round((totalAccepted * 100.0) / totalSubmissions)
                : 0;

        int streak = 0, totalActiveDays = 0;
        if (userCalendar != null) {
            streak = userCalendar.get("streak") != null
                    ? ((Number) userCalendar.get("streak")).intValue() : 0;
            totalActiveDays = userCalendar.get("totalActiveDays") != null
                    ? ((Number) userCalendar.get("totalActiveDays")).intValue() : 0;
        }

        Map<String, Integer> totals = fetchTotalQuestionCounts();

        return Map.ofEntries(
                Map.entry("username", username),
                Map.entry("ranking", ranking),
                Map.entry("easySolved", easySolved),
                Map.entry("mediumSolved", mediumSolved),
                Map.entry("hardSolved", hardSolved),
                Map.entry("totalSolved", easySolved + mediumSolved + hardSolved),
                Map.entry("acceptanceRate", acceptanceRate),
                Map.entry("streak", streak),
                Map.entry("totalActiveDays", totalActiveDays),
                Map.entry("easyTotal", totals.getOrDefault("Easy", 0)),
                Map.entry("mediumTotal", totals.getOrDefault("Medium", 0)),
                Map.entry("hardTotal", totals.getOrDefault("Hard", 0))
        );
    }

    // ── Sync my LC data to DB ─────────────────────────────────────────────────
    @Transactional
    public Map<String, Object> syncMyLCData() {
        User user = userService.getCurrentUser();

        if (user.getLeetcodeHandle() == null) {
            throw new RuntimeException("LeetCode handle not connected! Go to Profile first.");
        }

        Map<String, Object> lcData = getLCProfile(user.getLeetcodeHandle());

        // Save ranking as rating in DB, plus acceptance rate + streak
        user.setLeetcodeRating((Integer) lcData.get("ranking"));
        user.setLeetcodeRank("Rank #" + lcData.get("ranking"));
        user.setLeetcodeAcceptanceRate((Integer) lcData.get("acceptanceRate"));
        user.setLeetcodeStreak((Integer) lcData.get("streak"));
        user.setLeetcodeTotalActiveDays((Integer) lcData.get("totalActiveDays"));
        userRepository.save(user);

        return lcData;
    }

    // ── Get my LC profile ─────────────────────────────────────────────────────
    public Map<String, Object> getMyLCProfile() {
        User user = userService.getCurrentUser();

        if (user.getLeetcodeHandle() == null) {
            throw new RuntimeException("LeetCode handle not connected!");
        }

        return getLCProfile(user.getLeetcodeHandle());
    }
}
