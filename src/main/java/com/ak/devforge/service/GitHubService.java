package com.ak.devforge.service;

import com.ak.devforge.model.User;
import com.ak.devforge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GitHubService {

    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${app.github.token:}")
    private String githubToken;

    @Value("${app.github.api-url:https://api.github.com}")
    private String githubApiUrl;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.github.com")
            .defaultHeader("Accept", "application/vnd.github.v3+json")
            .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
            .build();

    public GitHubService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // ── Build request with optional token ────────────────────────────────────
    private WebClient.RequestHeadersSpec<?> buildRequest(String path) {
        WebClient.RequestHeadersSpec<?> req = webClient.get().uri(path);
        if (githubToken != null && !githubToken.isBlank()) {
            req = webClient.get().uri(path)
                    .header("Authorization", "Bearer " + githubToken);
        }
        return req;
    }

    // ── Fetch GitHub Profile ──────────────────────────────────────────────────
    public Map<String, Object> getGitHubProfile(String handle) {
        if (handle == null || handle.isBlank()) {
            throw new RuntimeException("GitHub handle is empty");
        }

        // ── User info ─────────────────────────────────────────────────────────
        Map userInfo;
        try {
            userInfo = buildRequest("/users/" + handle)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("GitHub user '" + handle + "' not found (HTTP " + e.getStatusCode() + ")");
        } catch (Exception e) {
            throw new RuntimeException("Could not reach GitHub API: " + e.getMessage());
        }

        if (userInfo == null || userInfo.containsKey("message")) {
            throw new RuntimeException("GitHub user not found: " + handle);
        }

        // ── Repos ─────────────────────────────────────────────────────────────
        List<Map> repos = List.of();
        try {
            repos = buildRequest("/users/" + handle + "/repos?sort=updated&per_page=10")
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .collectList()
                    .block();
        } catch (Exception ignored) { /* public repos unavailable — continue */ }

        // ── Public events for contribution count ──────────────────────────────
        List<Map> events = List.of();
        try {
            events = buildRequest("/users/" + handle + "/events/public?per_page=100")
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .collectList()
                    .block();
        } catch (Exception ignored) { /* events unavailable — continue */ }

        // ── Parse fields safely ───────────────────────────────────────────────
        int publicRepos = userInfo.get("public_repos") != null
                ? ((Number) userInfo.get("public_repos")).intValue() : 0;
        int followers = userInfo.get("followers") != null
                ? ((Number) userInfo.get("followers")).intValue() : 0;
        int following = userInfo.get("following") != null
                ? ((Number) userInfo.get("following")).intValue() : 0;

        long contributions = events.stream()
                .filter(e -> List.of("PushEvent", "PullRequestEvent", "IssuesEvent", "CreateEvent")
                        .contains(e.get("type")))
                .count();

        String topLanguage = getTopLanguage(repos);
        int githubScore = Math.min(100,
                followers * 2 + publicRepos * 3 + (int) contributions / 5);

        // ── Build recent repos list ───────────────────────────────────────────
        List<Map<String, Object>> recentRepos = repos.stream()
                .limit(6)
                .map(r -> {
                    Map<String, Object> repo = new HashMap<>();
                    repo.put("name", r.getOrDefault("name", ""));
                    repo.put("description", r.getOrDefault("description", ""));
                    repo.put("stars", r.getOrDefault("stargazers_count", 0));
                    repo.put("forks", r.getOrDefault("forks_count", 0));
                    repo.put("lang", r.getOrDefault("language", ""));
                    repo.put("updatedAt", r.getOrDefault("updated_at", ""));
                    return repo;
                })
                .toList();

        // ── Build response ────────────────────────────────────────────────────
        Map<String, Object> result = new HashMap<>();
        result.put("username", userInfo.getOrDefault("login", handle));
        result.put("name", userInfo.getOrDefault("name", handle));
        result.put("bio", userInfo.getOrDefault("bio", ""));
        result.put("avatarUrl", userInfo.getOrDefault("avatar_url", ""));
        result.put("publicRepos", publicRepos);
        result.put("followers", followers);
        result.put("following", following);
        result.put("totalContributions", contributions);
        result.put("topLanguage", topLanguage);
        result.put("githubScore", githubScore);
        result.put("recentRepos", recentRepos);
        result.put("profileUrl", "https://github.com/" + handle);
        return result;
    }

    // ── Get My GitHub Profile ─────────────────────────────────────────────────
    public Map<String, Object> getMyGitHubProfile() {
        User user = userService.getCurrentUser();
        if (user.getGithubHandle() == null || user.getGithubHandle().isBlank()) {
            throw new RuntimeException("GitHub handle not connected! Go to Profile → Edit → GitHub Handle.");
        }
        return getGitHubProfile(user.getGithubHandle());
    }

    // ── Sync GitHub Data to DB ────────────────────────────────────────────────
    @Transactional
    public Map<String, Object> syncMyGitHubData() {
        User user = userService.getCurrentUser();
        if (user.getGithubHandle() == null || user.getGithubHandle().isBlank()) {
            throw new RuntimeException("GitHub handle not connected!");
        }

        Map<String, Object> data = getGitHubProfile(user.getGithubHandle());

        if (data.get("totalContributions") != null)
            user.setGithubContributions(((Number) data.get("totalContributions")).intValue());
        if (data.get("publicRepos") != null)
            user.setGithubRepos(((Number) data.get("publicRepos")).intValue());
        if (data.get("topLanguage") != null)
            user.setGithubTopLanguage((String) data.get("topLanguage"));
        if (data.get("githubScore") != null)
            user.setGithubScore(((Number) data.get("githubScore")).intValue());

        userRepository.save(user);
        return data;
    }

    // ── Helper: Top Language ──────────────────────────────────────────────────
    private String getTopLanguage(List<Map> repos) {
        if (repos == null || repos.isEmpty()) return "Not available";
        Map<String, Integer> langCount = new HashMap<>();
        for (Map repo : repos) {
            String lang = (String) repo.get("language");
            if (lang != null) langCount.merge(lang, 1, Integer::sum);
        }
        return langCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Not available");
    }
}