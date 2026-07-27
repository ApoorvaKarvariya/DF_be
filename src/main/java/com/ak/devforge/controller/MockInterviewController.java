package com.ak.devforge.controller;

import com.ak.devforge.dto.request.MockAnswerRequest;
import com.ak.devforge.dto.request.MockInterviewRequest;
import com.ak.devforge.dto.response.AiFeedbackResponse;
import com.ak.devforge.dto.response.MockInterviewResponse;
import com.ak.devforge.service.MockInterviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class MockInterviewController {

    private final MockInterviewService mockInterviewService;

    public MockInterviewController(MockInterviewService mockInterviewService) {
        this.mockInterviewService = mockInterviewService;
    }

    // POST /api/interviews/start
    @PostMapping("/start")
    public ResponseEntity<MockInterviewResponse> startInterview(
            @Valid @RequestBody MockInterviewRequest request) {
        return ResponseEntity.ok(mockInterviewService.startInterview(request));
    }

    // POST /api/interviews/submit
    @PostMapping("/submit")
    public ResponseEntity<AiFeedbackResponse> submitAnswer(
            @Valid @RequestBody MockAnswerRequest request) {
        return ResponseEntity.ok(mockInterviewService.submitAnswer(request));
    }

    // GET /api/interviews
    @GetMapping
    public ResponseEntity<List<MockInterviewResponse>> getMyInterviews() {
        return ResponseEntity.ok(mockInterviewService.getMyInterviews());
    }

    // GET /api/interviews/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MockInterviewResponse> getInterview(
            @PathVariable Long id) {
        return ResponseEntity.ok(mockInterviewService.getInterviewById(id));
    }
}