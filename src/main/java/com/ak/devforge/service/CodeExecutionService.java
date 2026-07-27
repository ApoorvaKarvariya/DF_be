package com.ak.devforge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class CodeExecutionService {

    @Value("${app.judge0.api-url:https://ce.judge0.com}")
    private String judge0Url;

    private final WebClient webClient = WebClient.builder().build();

    // Judge0 language IDs — full list: https://ce.judge0.com/languages/
    private static final Map<String, Integer> LANGUAGE_IDS = Map.of(
            "java",       62,   // Java (OpenJDK 13.0.1)
            "python",     71,   // Python (3.8.1)
            "javascript", 63,   // JavaScript (Node.js 12.14.0)
            "cpp",        54,   // C++ (GCC 9.2.0)
            "go",         60    // Go (1.13.5)
    );

    public Map<String, Object> executeCode(String language, String code) {

        Integer langId = LANGUAGE_IDS.get(language.toLowerCase());
        if (langId == null) {
            return Map.of(
                    "stdout", "",
                    "stderr", "Unsupported language: " + language,
                    "exitCode", 1,
                    "language", language
            );
        }

        // Base64 encode source code
        String encodedCode = Base64.getEncoder().encodeToString(code.getBytes());

        Map<String, Object> submissionBody = Map.of(
                "language_id", langId,
                "source_code", encodedCode
        );

        try {
            // ── Step 1: Submit ──────────────────────────────────────────────
            Map submissionResponse = webClient.post()
                    .uri(judge0Url + "/submissions?base64_encoded=true&wait=false")
                    .header("Content-Type", "application/json")
                    .bodyValue(submissionBody)
                    .exchangeToMono(res -> res.bodyToMono(Map.class))
                    .timeout(Duration.ofSeconds(15))
                    .block();

            if (submissionResponse == null || !submissionResponse.containsKey("token")) {
                return Map.of(
                        "stdout", "",
                        "stderr", "Failed to submit to Judge0: " + submissionResponse,
                        "exitCode", 1,
                        "language", language
                );
            }

            String token = (String) submissionResponse.get("token");
            System.out.println("Judge0 token: " + token);

            // ── Step 2: Poll for result ─────────────────────────────────────
            // Status IDs: 1=In Queue, 2=Processing, 3=Accepted, 4+=Error/TLE etc.
            Map result = null;
            for (int attempt = 0; attempt < 10; attempt++) {
                Thread.sleep(1000);

                result = webClient.get()
                        .uri(judge0Url + "/submissions/" + token + "?base64_encoded=true")
                        .exchangeToMono(res -> res.bodyToMono(Map.class))
                        .timeout(Duration.ofSeconds(10))
                        .block();

                if (result == null) continue;

                Map<String, Object> statusObj = (Map<String, Object>) result.get("status");
                int statusId = statusObj != null ? (int) statusObj.get("id") : 0;
                System.out.println("Judge0 status [attempt " + (attempt + 1) + "]: " + statusId);

                if (statusId > 2) break; // done
            }

            if (result == null) {
                return Map.of("stdout", "", "stderr", "Timed out waiting for Judge0", "exitCode", 1, "language", language);
            }

            // ── Step 3: Decode output ───────────────────────────────────────
            String stdout = decodeBase64Field(result.get("stdout"));
            String stderr = decodeBase64Field(result.get("stderr"));
            String compileOutput = decodeBase64Field(result.get("compile_output"));

            if (!compileOutput.isEmpty()) {
                stderr = compileOutput + (stderr.isEmpty() ? "" : "\n" + stderr);
            }

            Object exitCodeObj = result.get("exit_code");
            int exitCode = exitCodeObj != null ? ((Number) exitCodeObj).intValue() : 0;

            String execTime = "";
            Object timeObj = result.get("time");
            if (timeObj != null) {
                try {
                    double secs = Double.parseDouble(timeObj.toString());
                    execTime = (int)(secs * 1000) + "ms";
                } catch (NumberFormatException ignored) {}
            }

            return Map.of(
                    "stdout", stdout,
                    "stderr", stderr,
                    "exitCode", exitCode,
                    "language", language,
                    "executionTime", execTime
            );

        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return Map.of("stdout", "", "stderr", "Execution interrupted", "exitCode", 1, "language", language);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("stdout", "", "stderr", e.getMessage() != null ? e.getMessage() : "Unknown error", "exitCode", 1, "language", language);
        }
    }

    private String decodeBase64Field(Object value) {
        if (value == null) return "";
        try {
            return new String(Base64.getDecoder().decode(value.toString().trim()));
        } catch (Exception e) {
            return value.toString();
        }
    }

    public List<String> getSupportedLanguages() {
        return List.of("java", "python", "javascript", "cpp", "go");
    }
}
