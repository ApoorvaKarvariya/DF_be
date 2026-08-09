package com.ak.devforge.controller;

import com.ak.devforge.dto.request.ForgotPasswordRequest;
import com.ak.devforge.dto.request.LoginRequest;
import com.ak.devforge.dto.request.RegisterRequest;
import com.ak.devforge.dto.request.ResetPasswordRequest;
import com.ak.devforge.dto.response.AuthResponse;
import com.ak.devforge.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        // Generic response regardless of whether the email exists, to avoid leaking user data
        return ResponseEntity.ok(Map.of(
                "message", "If an account with that email exists, a password reset link has been sent."
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(Map.of(
                "message", "Your password has been reset successfully. You can now sign in."
        ));
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("DevForge API is running!");
    }
}