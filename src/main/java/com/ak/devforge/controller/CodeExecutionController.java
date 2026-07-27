package com.ak.devforge.controller;

import com.ak.devforge.service.CodeExecutionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/code")

public class CodeExecutionController {

    private final CodeExecutionService codeExecutionService;

    public CodeExecutionController(CodeExecutionService codeExecutionService) {
        this.codeExecutionService = codeExecutionService;
    }

    // POST /api/code/run
    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> runCode(@RequestBody Map<String, String> request) {
        String language = request.getOrDefault("language", "java");
        String code = request.getOrDefault("code", "");
        return ResponseEntity.ok(codeExecutionService.executeCode(language, code));
    }

    // GET /api/code/languages
    @GetMapping("/languages")
    public ResponseEntity<List<String>> getLanguages() {
        return ResponseEntity.ok(codeExecutionService.getSupportedLanguages());
    }
}
