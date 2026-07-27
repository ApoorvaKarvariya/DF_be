package com.ak.devforge.service;

import com.ak.devforge.dto.request.MockAnswerRequest;
import com.ak.devforge.dto.request.MockInterviewRequest;
import com.ak.devforge.dto.response.AiFeedbackResponse;
import com.ak.devforge.dto.response.MockInterviewResponse;
import com.ak.devforge.exception.BadRequestException;
import com.ak.devforge.exception.ResourceNotFoundException;
import com.ak.devforge.model.MockAnswer;
import com.ak.devforge.model.MockInterview;
import com.ak.devforge.model.User;
import com.ak.devforge.repository.MockAnswerRepository;
import com.ak.devforge.repository.MockInterviewRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class MockInterviewService {

    private final MockInterviewRepository mockInterviewRepository;
    private final MockAnswerRepository mockAnswerRepository;
    private final UserService userService;
    private final AiService aiService;

    public MockInterviewService(MockInterviewRepository mockInterviewRepository, MockAnswerRepository mockAnswerRepository, UserService userService, AiService aiService) {
        this.mockInterviewRepository = mockInterviewRepository;
        this.mockAnswerRepository = mockAnswerRepository;
        this.userService = userService;
        this.aiService = aiService;
    }

    // ─── Start New Interview ──────────────────────────────────────────────────

    @Transactional
    public MockInterviewResponse startInterview(MockInterviewRequest request) {
        User user = userService.getCurrentUser();

        // Generate AI question
        String aiQuestion = aiService.generateInterviewQuestion(
                request.getCategory(),
                request.getDifficulty(),
                request.getTargetCompany());

        MockInterview interview = MockInterview.builder()
                .user(user)
                .targetCompany(request.getTargetCompany())
                .category(request.getCategory())
                .difficulty(request.getDifficulty())
                .aiGeneratedQuestion(aiQuestion)
                .status(MockInterview.Status.IN_PROGRESS)
                .build();

        mockInterviewRepository.save(interview);
        return mapToResponse(interview);
    }

    // ─── Submit Answer ────────────────────────────────────────────────────────

    @Transactional
    public AiFeedbackResponse submitAnswer(MockAnswerRequest request) {
        User user = userService.getCurrentUser();

        MockInterview interview = mockInterviewRepository
                .findById(request.getInterviewId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Interview session not found"));

        if (!interview.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("This is not your interview session!");
        }

        // Get AI feedback
        AiFeedbackResponse feedback = aiService.evaluateAnswer(
                request.getQuestion(),
                request.getUserAnswer(),
                interview.getCategory());

        // Save answer
        MockAnswer answer = MockAnswer.builder()
                .mockInterview(interview)
                .question(request.getQuestion())
                .userAnswer(request.getUserAnswer())
                .aiFeedback(feedback.getFeedback())
                .aiScore(feedback.getScore())
                .build();

        mockAnswerRepository.save(answer);

        // Update interview score
        interview.setAiScore(feedback.getScore());
        interview.setAiFeedback(feedback.getFeedback());
        interview.setStatus(MockInterview.Status.COMPLETED);
        interview.setCompletedAt(LocalDateTime.now());
        mockInterviewRepository.save(interview);

        return feedback;
    }

    // ─── Get My Interviews ────────────────────────────────────────────────────

    public List<MockInterviewResponse> getMyInterviews() {
        User user = userService.getCurrentUser();
        return mockInterviewRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ─── Get Interview by ID ──────────────────────────────────────────────────

    public MockInterviewResponse getInterviewById(Long id) {
        MockInterview interview = mockInterviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Interview not found: " + id));
        return mapToResponse(interview);
    }

    // ─── Map to Response ──────────────────────────────────────────────────────

    private MockInterviewResponse mapToResponse(MockInterview interview) {
        List<AiFeedbackResponse> answers = mockAnswerRepository
                .findByMockInterview(interview)
                .stream()
                .map(a -> AiFeedbackResponse.builder()
                        .question(a.getQuestion())
                        .userAnswer(a.getUserAnswer())
                        .feedback(a.getAiFeedback())
                        .score(a.getAiScore())
                        .build())
                .collect(Collectors.toList());

        return MockInterviewResponse.builder()
                .id(interview.getId())
                .targetCompany(interview.getTargetCompany())
                .category(interview.getCategory())
                .difficulty(interview.getDifficulty())
                .aiGeneratedQuestion(interview.getAiGeneratedQuestion())
                .aiFeedback(interview.getAiFeedback())
                .aiScore(interview.getAiScore())
                .status(interview.getStatus())
                .answers(answers)
                .createdAt(interview.getCreatedAt())
                .completedAt(interview.getCompletedAt())
                .build();
    }
}