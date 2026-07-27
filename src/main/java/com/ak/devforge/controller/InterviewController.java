package com.ak.devforge.controller;

import com.ak.devforge.dto.request.QuestionRequest;
import com.ak.devforge.dto.response.QuestionResponse;
import com.ak.devforge.service.InterviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    // POST /api/questions (Admin only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuestionResponse> addQuestion(
            @Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(interviewService.addQuestion(request));
    }

    // GET /api/questions
    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getAllQuestions() {
        return ResponseEntity.ok(interviewService.getAllQuestions());
    }

    // GET /api/questions/filter
    @GetMapping("/filter")
    public ResponseEntity<List<QuestionResponse>> filterQuestions(
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String topic) {
        return ResponseEntity.ok(interviewService.filterQuestions(
                difficulty, category, company, topic));
    }

    // GET /api/questions/search
    @GetMapping("/search")
    public ResponseEntity<List<QuestionResponse>> searchQuestions(
            @RequestParam String keyword) {
        return ResponseEntity.ok(interviewService.searchQuestions(keyword));
    }

    // GET /api/questions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(interviewService.getQuestionById(id));
    }

    // POST /api/questions/{id}/save
    @PostMapping("/{id}/save")
    public ResponseEntity<String> toggleSave(@PathVariable Long id) {
        return ResponseEntity.ok(interviewService.toggleSaveQuestion(id));
    }

    // GET /api/questions/saved
    @GetMapping("/saved")
    public ResponseEntity<List<QuestionResponse>> getSaved() {
        return ResponseEntity.ok(interviewService.getSavedQuestions());
    }
}