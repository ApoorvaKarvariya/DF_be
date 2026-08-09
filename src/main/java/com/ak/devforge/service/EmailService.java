package com.ak.devforge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.brevo.api-key}")
    private String brevoApiKey;

    @Value("${app.brevo.sender-email}")
    private String senderEmail;

    @Value("${app.brevo.sender-name:DevForge}")
    private String senderName;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.brevo.com/v3")
            .build();

    // ── Send the password reset email via Brevo transactional email API ──────
    public void sendPasswordResetEmail(String toEmail, String toName, String resetLink) {

        Map<String, Object> requestBody = Map.of(
                "sender", Map.of(
                        "name", senderName,
                        "email", senderEmail
                ),
                "to", java.util.List.of(
                        Map.of(
                                "email", toEmail,
                                "name", toName != null && !toName.isBlank() ? toName : toEmail
                        )
                ),
                "subject", "Reset your DevForge password",
                "htmlContent", buildResetEmailHtml(toName, resetLink)
        );

        try {
            webClient.post()
                    .uri("/smtp/email")
                    .header("api-key", brevoApiKey)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            logger.info("Password reset email sent to {}", toEmail);

        } catch (WebClientResponseException e) {
            logger.error("Brevo API error while sending reset email: {} - {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to send password reset email");
        } catch (Exception e) {
            logger.error("Unexpected error while sending reset email", e);
            throw new RuntimeException("Failed to send password reset email");
        }
    }

    // ── Simple HTML template for the reset email ──────────────────────────────
    private String buildResetEmailHtml(String toName, String resetLink) {
        String greetingName = (toName != null && !toName.isBlank()) ? toName : "there";
        return """
                <div style="font-family:'Segoe UI',Arial,sans-serif;max-width:480px;margin:0 auto;padding:32px 24px;background:#f4f5fa;border-radius:12px;">
                  <h2 style="color:#1a1b2e;margin-bottom:8px;">Reset your password</h2>
                  <p style="color:#4b4f6b;font-size:14px;line-height:1.5;">Hi %s,</p>
                  <p style="color:#4b4f6b;font-size:14px;line-height:1.5;">
                    We received a request to reset the password for your DevForge account.
                    Click the button below to choose a new password. This link expires in 30 minutes.
                  </p>
                  <div style="text-align:center;margin:28px 0;">
                    <a href="%s" style="background:#6366f1;color:#ffffff;text-decoration:none;
                       padding:12px 28px;border-radius:8px;font-weight:600;font-size:14px;display:inline-block;">
                      Reset Password
                    </a>
                  </div>
                  <p style="color:#7b7f9e;font-size:12px;line-height:1.5;">
                    If you didn't request this, you can safely ignore this email — your password will remain unchanged.
                  </p>
                  <p style="color:#7b7f9e;font-size:12px;">— The DevForge Team</p>
                </div>
                """.formatted(greetingName, resetLink);
    }
}
