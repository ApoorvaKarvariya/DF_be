package com.ak.devforge.service;

import com.ak.devforge.dto.response.ResumeAnalysisResponse;
import com.ak.devforge.exception.BadRequestException;
import com.ak.devforge.model.User;
import com.ak.devforge.repository.UserRepository;


import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service


public class ResumeService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AiService aiService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public ResumeService(UserRepository userRepository, UserService userService, AiService aiService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.aiService = aiService;
    }

    // ─── Upload & Analyze Resume ──────────────────────────────────────────────

    @Transactional
    public ResumeAnalysisResponse uploadAndAnalyze(MultipartFile file) {

        // Validate file
        if (file.isEmpty()) {
            throw new BadRequestException("Please upload a PDF file!");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null ||
                !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new BadRequestException("Only PDF files are allowed!");
        }

        User user = userService.getCurrentUser();

        // Save file to disk
        String savedPath = saveFile(file, user.getId());

        // Extract text from PDF
        String extractedText = extractTextFromPdf(savedPath);

        // Extract skills from text
        List<String> skills = extractSkills(extractedText);

        // Get AI analysis
        ResumeAnalysisResponse analysis = aiService.analyzeResume(
                extractedText, skills);

        // Update user in DB
        user.setResumePath(savedPath);
        user.setAtsScore(analysis.getAtsScore());
        userRepository.save(user);

        analysis.setExtractedText(extractedText.substring(
                0, Math.min(500, extractedText.length())) + "...");
        analysis.setExtractedSkills(skills);
        analysis.setResumePath(savedPath);

        return analysis;
    }

    // ─── Save File ────────────────────────────────────────────────────────────

    private String saveFile(MultipartFile file, Long userId) {
        try {
            Path uploadPath = Paths.get(uploadDir + "/" + userId);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_resume.pdf";
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            return filePath.toString();

        } catch (IOException e) {

            throw new BadRequestException("Could not save file!");
        }
    }

    // ─── Extract Text from PDF ────────────────────────────────────────────────

    private String extractTextFromPdf(String filePath) {
        try {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(
                    Loader.loadPDF(new File(filePath))
            );
        } catch (IOException e) {

            throw new BadRequestException("Could not read PDF file!");
        }
    }

    // ─── Extract Skills ───────────────────────────────────────────────────────

    private List<String> extractSkills(String text) {
        List<String> allSkills = Arrays.asList(
                "Java", "Python", "JavaScript", "TypeScript", "C++", "C#",
                "Spring Boot", "Spring", "React", "Angular", "Vue", "Node.js",
                "MySQL", "PostgreSQL", "MongoDB", "Redis", "Oracle",
                "AWS", "Azure", "GCP", "Docker", "Kubernetes", "Jenkins",
                "Git", "GitHub", "GitLab", "Maven", "Gradle",
                "HTML", "CSS", "REST API", "GraphQL", "Microservices",
                "Hibernate", "JPA", "Kafka", "RabbitMQ",
                "Machine Learning", "Deep Learning", "TensorFlow", "PyTorch",
                "Data Structures", "Algorithms", "System Design",
                "Agile", "Scrum", "Linux", "Unix"
        );

        String lowerText = text.toLowerCase();
        return allSkills.stream()
                .filter(skill -> lowerText.contains(skill.toLowerCase()))
                .collect(Collectors.toList());
    }

    // ─── Get My Resume Analysis ───────────────────────────────────────────────

    public ResumeAnalysisResponse getMyResumeAnalysis() {
        User user = userService.getCurrentUser();

        if (user.getResumePath() == null) {
            throw new BadRequestException("No resume uploaded yet!");
        }

        String extractedText = extractTextFromPdf(user.getResumePath());
        List<String> skills = extractSkills(extractedText);
        ResumeAnalysisResponse analysis = aiService.analyzeResume(
                extractedText, skills);

        analysis.setExtractedSkills(skills);
        analysis.setResumePath(user.getResumePath());
        analysis.setAtsScore(user.getAtsScore());

        return analysis;
    }
}