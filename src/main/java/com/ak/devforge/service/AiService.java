package com.ak.devforge.service;

import com.ak.devforge.dto.response.AiFeedbackResponse;
import com.ak.devforge.dto.response.ResumeAnalysisResponse;
import com.ak.devforge.model.Question;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service

public class AiService {

    @Value("${app.groq.api-key}")
    private String groqApiKey;

    @Value("${app.groq.api-url}")
    private String groqApiUrl;

    private final WebClient webClient = WebClient.builder().build();

    // =====================================================
    // GROQ API CALL
    // =====================================================

    private String callGroq(String prompt) {

        try {



            Map<String, Object> requestBody = Map.of(
                    "model", "openai/gpt-oss-120b",
                    "messages", List.of(
                            Map.of(
                                    "role", "user",
                                    "content", prompt
                            )
                    ),
                    "temperature", 0.7
            );

            Map response = webClient.post()
                    .uri(groqApiUrl)
                    .header("Authorization", "Bearer " + groqApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("choices")) {

                List<Map<String, Object>> choices =
                        (List<Map<String, Object>>) response.get("choices");

                Map<String, Object> message =
                        (Map<String, Object>) choices.get(0).get("message");

                return (String) message.get("content");
            }

        } catch (Exception e) {


            e.printStackTrace();
        }

        return "AI service temporarily unavailable.";
    }

    // =====================================================
    // GENERATE INTERVIEW QUESTION
    // =====================================================

    public String generateInterviewQuestion(
            Question.Category category,
            Question.Difficulty difficulty,
            String targetCompany) {

        String prompt = String.format("""
                You are an expert technical interviewer at %s.

                Generate ONE challenging %s level interview question for category: %s.

                Requirements:
                - Question should be clear and specific
                - For DSA: include problem statement with examples
                - For SYSTEM_DESIGN: include scale requirements
                - For HR/BEHAVIORAL: make it situational
                - For CS_FUNDAMENTALS: test deep understanding

                Return ONLY the question.
                """,
                targetCompany != null ? targetCompany : "a top tech company",
                difficulty.name(),
                category.name());

        return callGroq(prompt);
    }

    // =====================================================
    // EVALUATE ANSWER
    // =====================================================

    public AiFeedbackResponse evaluateAnswer(
            String question,
            String userAnswer,
            Question.Category category) {

        String prompt = String.format("""
                You are an expert technical interviewer evaluating a candidate's answer.

                Category: %s
                Question: %s
                Candidate Answer: %s

                Respond in EXACT format:

                SCORE: [0-100]
                STRENGTHS: [what was good]
                IMPROVEMENTS: [what could be better]
                IDEAL_ANSWER: [key points]
                FEEDBACK: [overall feedback]
                """,
                category.name(),
                question,
                userAnswer);

        String response = callGroq(prompt);

        return parseAiFeedback(question, userAnswer, response);
    }

    // =====================================================
    // WEAKNESS ANALYSIS
    // =====================================================

    public String generateWeaknessAnalysis(
            List<String> weakTopics,
            Integer lcRating) {

        String prompt = String.format("""
                You are a coding mentor.

                LeetCode Rating: %d
                Weak Topics: %s

                Create a personalized 7-day improvement plan.
                Give day-wise tasks.
                """,
                lcRating != null ? lcRating : 0,
                String.join(", ", weakTopics));

        return callGroq(prompt);
    }

    // =====================================================
    // RESPONSE PARSER
    // =====================================================

    private AiFeedbackResponse parseAiFeedback(
            String question,
            String userAnswer,
            String aiResponse) {

        int scoreInt = 50;

        try {

            String score =
                    extractSection(aiResponse, "SCORE:");

            scoreInt = Integer.parseInt(
                    score.replaceAll("[^0-9]", "")
            );

        } catch (Exception ignored) {
        }

        return AiFeedbackResponse.builder()
                .question(question)
                .userAnswer(userAnswer)
                .score(scoreInt)
                .strengths(extractSection(aiResponse, "STRENGTHS:"))
                .improvements(extractSection(aiResponse, "IMPROVEMENTS:"))
                .idealAnswer(extractSection(aiResponse, "IDEAL_ANSWER:"))
                .feedback(extractSection(aiResponse, "FEEDBACK:"))
                .build();
    }

    private String extractSection(String text, String section) {

        try {

            int start =
                    text.indexOf(section) + section.length();

            int end =
                    text.indexOf("\n", start);

            if (end == -1)
                end = text.length();

            return text.substring(start, end).trim();

        } catch (Exception e) {

            return "";
        }
    }
    // ─── Analyze Resume ───────────────────────────────────────────────────────

    public ResumeAnalysisResponse analyzeResume(
            String resumeText, List<String> skills) {

        String prompt = String.format("""
            You are an expert HR and ATS (Applicant Tracking System) specialist.
            
            Analyze this resume and respond in EXACT format:
            ATS_SCORE: [0-100]
            STRENGTHS: [what's good about this resume]
            WEAKNESSES: [what's missing or weak]
            SUGGESTIONS: [specific improvements]
            OVERALL_FEEDBACK: [2-3 sentence summary]
            
            Skills found: %s
            
            Resume content:
            %s
            """,
                String.join(", ", skills),
                resumeText.substring(0, Math.min(2000, resumeText.length())));

        String response = callGroq(prompt);

        int atsScore = 60;
        try {
            String scoreStr = extractSection(response, "ATS_SCORE:");
            atsScore = Integer.parseInt(scoreStr.trim().replaceAll("[^0-9]", ""));
        } catch (Exception ignored) {}

        return ResumeAnalysisResponse.builder()
                .atsScore(atsScore)
                .strengths(extractSection(response, "STRENGTHS:"))
                .weaknesses(extractSection(response, "WEAKNESSES:"))
                .suggestions(extractSection(response, "SUGGESTIONS:"))
                .overallFeedback(extractSection(response, "OVERALL_FEEDBACK:"))
                .build();
    }

    public String generateResumeContent(String prompt) {
        return callGroq(prompt);
    }
}