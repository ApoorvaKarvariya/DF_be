package com.ak.devforge.controller;

import com.ak.devforge.dto.request.LoginRequest;
import com.ak.devforge.dto.request.RegisterRequest;
import com.ak.devforge.dto.response.AuthResponse;
import com.ak.devforge.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("DevForge API is running!");
    }
}