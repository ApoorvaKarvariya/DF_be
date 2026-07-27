package com.ak.devforge.service;

import com.ak.devforge.dto.response.ResumeAnalysisResponse;
import com.ak.devforge.model.User;
import com.ak.devforge.repository.UserRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class ResumeGeneratorService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AiService aiService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public ResumeGeneratorService(UserService userService, UserRepository userRepository, AiService aiService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.aiService = aiService;
    }

    // ── Generate Resume from Profile ──────────────────────────────────────────
    @Transactional
    public Map<String, Object> generateResume() {
        User user = userService.getCurrentUser();

        // Build prompt with user data
        String prompt = buildPrompt(user);

        // Call Groq AI to generate resume content
        String resumeContent = callGroqForResume(prompt);

        // Generate PDF from content
        String pdfPath = generatePdf(resumeContent, user);

        // Update user resume path
        user.setResumePath(pdfPath);
        userRepository.save(user);

        return Map.of(
            "message", "Resume generated successfully!",
            "resumePath", pdfPath,
            "content", resumeContent
        );
    }

    // ── Build Groq Prompt ─────────────────────────────────────────────────────
    private String buildPrompt(User user) {
        return String.format("""
            You are an expert technical resume writer specializing in ATS-optimized resumes.
            
            Create a professional resume for this developer:
            
            Name: %s
            College: %s
            Target Company: %s
            Bio: %s
            LeetCode Solved: problems solved across Easy/Medium/Hard
            LeetCode Rank: %s | Acceptance Rate: %s%%
            
            Format the resume with EXACTLY these sections using these headers:
            
            SUMMARY
            [2-3 sentence professional summary]
            
            SKILLS
            [Comma separated technical skills]
            
            EDUCATION
            [Degree, College, Year]
            
            COMPETITIVE PROGRAMMING
            [CF and LC achievements]
            
            PROJECTS
            [2-3 impressive project descriptions with tech stack]
            
            ACHIEVEMENTS
            [Notable achievements, hackathons, etc]
            
            Rules:
            - Use action verbs (Developed, Implemented, Optimized, Designed)
            - Keep under 600 words total
            - Make it ATS-friendly with relevant keywords
            - Be specific with numbers and metrics
            """,
            user.getFullName(),
            user.getCollege() != null ? user.getCollege() : "Not specified",
            user.getTargetCompany() != null ? user.getTargetCompany() : "Top Tech Company",
            user.getBio() != null ? user.getBio() : "Passionate developer",
            user.getLeetcodeRank() != null ? user.getLeetcodeRank() : "Not linked",
            user.getLeetcodeAcceptanceRate() != null ? user.getLeetcodeAcceptanceRate() : "Not linked"
        );
    }

    // ── Call Groq for Resume Content ──────────────────────────────────────────
    private String callGroqForResume(String prompt) {
        try {
            // Reuse AiService's internal method via reflection workaround
            // We'll use a public wrapper
            return aiService.generateResumeContent(prompt);
        } catch (Exception e) {
            return getDefaultResumeContent();
        }
    }

    // ── Generate PDF using PDFBox ─────────────────────────────────────────────
    private String generatePdf(String content, User user) {
        try {
            // Create directory
            String dirPath = uploadDir + "/" + user.getId() + "/generated";
            Files.createDirectories(Paths.get(dirPath));
            String filePath = dirPath + "/ai_resume.pdf";

            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float pageWidth = page.getMediaBox().getWidth() - 2 * margin;
            float y = yStart;

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Title - Full Name
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, y);
            contentStream.showText(user.getFullName() != null ? user.getFullName() : "Developer");
            contentStream.endText();
            y -= 20;

            // Contact info
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, y);
            contentStream.showText(user.getEmail() + " | @" + user.getUsername());
            contentStream.endText();
            y -= 25;

            // Draw separator line
            contentStream.setLineWidth(0.5f);
            contentStream.moveTo(margin, y);
            contentStream.lineTo(page.getMediaBox().getWidth() - margin, y);
            contentStream.stroke();
            y -= 15;

            // Parse and write sections
            String[] lines = content.split("\n");
            for (String line : lines) {
                if (y < margin + 50) {
                    // New page if needed
                    contentStream.close();
                    PDPage newPage = new PDPage(PDRectangle.A4);
                    document.addPage(newPage);
                    contentStream = new PDPageContentStream(document, newPage);
                    y = yStart;
                }

                line = line.trim();
                if (line.isEmpty()) {
                    y -= 8;
                    continue;
                }

                // Section headers (ALL CAPS lines)
                boolean isHeader = line.equals(line.toUpperCase()) && line.length() > 2
                    && !line.startsWith("-") && !line.startsWith("•");

                if (isHeader) {
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, y);
                    contentStream.showText(line);
                    contentStream.endText();
                    y -= 5;
                    // Underline
                    contentStream.setLineWidth(0.3f);
                    contentStream.moveTo(margin, y);
                    contentStream.lineTo(page.getMediaBox().getWidth() - margin, y);
                    contentStream.stroke();
                    y -= 12;
                } else {
                    // Regular content - handle long lines
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
                    // Wrap long text
                    List<String> wrappedLines = wrapText(line, pageWidth, 10);
                    for (String wrappedLine : wrappedLines) {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin + 10, y);
                        contentStream.showText(sanitizeText(wrappedLine));
                        contentStream.endText();
                        y -= 14;
                    }
                }
            }

            contentStream.close();
            document.save(filePath);
            document.close();
            return filePath;

        } catch (IOException e) {
            throw new RuntimeException("Could not generate PDF: " + e.getMessage());
        }
    }

    // ── Text utilities ────────────────────────────────────────────────────────
    private List<String> wrapText(String text, float maxWidth, float fontSize) {
        // Simple word wrap - approx 0.6 * fontSize per char
        int maxChars = (int) (maxWidth / (fontSize * 0.5));
        if (text.length() <= maxChars) return List.of(text);

        List<String> lines = new java.util.ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder current = new StringBuilder();
        for (String word : words) {
            if (current.length() + word.length() + 1 > maxChars) {
                if (current.length() > 0) lines.add(current.toString().trim());
                current = new StringBuilder(word + " ");
            } else {
                current.append(word).append(" ");
            }
        }
        if (current.length() > 0) lines.add(current.toString().trim());
        return lines;
    }

    private String sanitizeText(String text) {
        if (text == null) return "";
        // Remove non-latin1 characters that PDFBox standard fonts can't handle
        return text.replaceAll("[^\\x00-\\xFF]", "")
                   .replaceAll("[\\x00-\\x1F]", "");
    }

    private String getDefaultResumeContent() {
        return """
            SUMMARY
            Passionate software developer with strong foundation in data structures and algorithms.
            
            SKILLS
            Java, Spring Boot, React, MySQL, Git, Data Structures, Algorithms
            
            EDUCATION
            B.Tech Computer Science
            
            COMPETITIVE PROGRAMMING
            Active competitive programmer on LeetCode.
            
            PROJECTS
            DevForge - Full stack developer growth platform built with Spring Boot and React.
            
            ACHIEVEMENTS
            Consistent competitive programmer with multiple contest participations.
            """;
    }
}
