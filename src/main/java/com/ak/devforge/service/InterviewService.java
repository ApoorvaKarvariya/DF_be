package com.ak.devforge.service;

import com.ak.devforge.dto.request.QuestionRequest;
import com.ak.devforge.dto.response.QuestionResponse;
import com.ak.devforge.exception.ResourceNotFoundException;
import com.ak.devforge.model.Question;
import com.ak.devforge.model.SavedQuestion;
import com.ak.devforge.model.User;
import com.ak.devforge.repository.QuestionRepository;
import com.ak.devforge.repository.SavedQuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewService {

    private final QuestionRepository questionRepository;
    private final SavedQuestionRepository savedQuestionRepository;
    private final UserService userService;

    public InterviewService(QuestionRepository questionRepository, SavedQuestionRepository savedQuestionRepository, UserService userService) {
        this.questionRepository = questionRepository;
        this.savedQuestionRepository = savedQuestionRepository;
        this.userService = userService;
    }

    // ─── Add Question (Admin) ─────────────────────────────────────────────────

    @Transactional
    public QuestionResponse addQuestion(QuestionRequest request) {
        Question question = Question.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .difficulty(request.getDifficulty())
                .category(request.getCategory())
                .company(request.getCompany())
                .topic(request.getTopic())
                .hints(request.getHints())
                .solutionApproach(request.getSolutionApproach())
                .leetcodeLink(request.getLeetcodeLink())
                .frequency(request.getFrequency())
                .build();

        questionRepository.save(question);
        return mapToResponse(question, false);
    }

    // ─── Get All Questions ────────────────────────────────────────────────────

    public List<QuestionResponse> getAllQuestions() {
        User user = userService.getCurrentUser();
        return questionRepository.findAll().stream()
                .map(q -> mapToResponse(q,
                        savedQuestionRepository.existsByUserAndQuestion(user, q)))
                .collect(Collectors.toList());
    }

    // ─── Filter Questions ─────────────────────────────────────────────────────

    public List<QuestionResponse> filterQuestions(
            String difficulty, String category,
            String company, String topic) {

        User user = userService.getCurrentUser();

        Question.Difficulty diff = difficulty != null ?
                Question.Difficulty.valueOf(difficulty.toUpperCase()) : null;
        Question.Category cat = category != null ?
                Question.Category.valueOf(category.toUpperCase()) : null;

        return questionRepository.filterQuestions(diff, cat, company, topic)
                .stream()
                .map(q -> mapToResponse(q,
                        savedQuestionRepository.existsByUserAndQuestion(user, q)))
                .collect(Collectors.toList());
    }

    // ─── Search Questions ─────────────────────────────────────────────────────

    public List<QuestionResponse> searchQuestions(String keyword) {
        User user = userService.getCurrentUser();
        return questionRepository.searchQuestions(keyword)
                .stream()
                .map(q -> mapToResponse(q,
                        savedQuestionRepository.existsByUserAndQuestion(user, q)))
                .collect(Collectors.toList());
    }

    // ─── Get Question by ID ───────────────────────────────────────────────────

    public QuestionResponse getQuestionById(Long id) {
        User user = userService.getCurrentUser();
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Question not found: " + id));
        return mapToResponse(question,
                savedQuestionRepository.existsByUserAndQuestion(user, question));
    }

    // ─── Save / Unsave Question ───────────────────────────────────────────────

    @Transactional
    public String toggleSaveQuestion(Long questionId) {
        User user = userService.getCurrentUser();
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Question not found: " + questionId));

        if (savedQuestionRepository.existsByUserAndQuestion(user, question)) {
            savedQuestionRepository.deleteByUserAndQuestion(user, question);
            return "Question removed from saved!";
        } else {
            savedQuestionRepository.save(SavedQuestion.builder()
                    .user(user)
                    .question(question)
                    .build());
            return "Question saved!";
        }
    }

    // ─── Get Saved Questions ──────────────────────────────────────────────────

    public List<QuestionResponse> getSavedQuestions() {
        User user = userService.getCurrentUser();
        return savedQuestionRepository.findByUser(user)
                .stream()
                .map(sq -> mapToResponse(sq.getQuestion(), true))
                .collect(Collectors.toList());
    }

    // ─── Map to Response ──────────────────────────────────────────────────────

    public QuestionResponse mapToResponse(Question q, Boolean isSaved) {
        return QuestionResponse.builder()
                .id(q.getId())
                .title(q.getTitle())
                .description(q.getDescription())
                .difficulty(q.getDifficulty())
                .category(q.getCategory())
                .company(q.getCompany())
                .topic(q.getTopic())
                .hints(q.getHints())
                .solutionApproach(q.getSolutionApproach())
                .leetcodeLink(q.getLeetcodeLink())
                .frequency(q.getFrequency())
                .isSaved(isSaved)
                .createdAt(q.getCreatedAt())
                .build();
    }
}