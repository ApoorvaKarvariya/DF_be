package com.ak.devforge.controller;

import com.ak.devforge.dto.response.ResumeAnalysisResponse;
import com.ak.devforge.service.ResumeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")

public class ResumeController {


    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    // POST /api/resume/upload
    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeAnalysisResponse> uploadResume(
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(resumeService.uploadAndAnalyze(file));
    }

    // GET /api/resume/analysis
    @GetMapping("/analysis")
    public ResponseEntity<ResumeAnalysisResponse> getAnalysis() {
        return ResponseEntity.ok(resumeService.getMyResumeAnalysis());
    }
}