package com.ak.devforge.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "mock_interviews")
public class MockInterview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String targetCompany;

    @Enumerated(EnumType.STRING)
    private Question.Category category;

    @Enumerated(EnumType.STRING)
    private Question.Difficulty difficulty;

    @Column(columnDefinition = "TEXT")
    private String aiGeneratedQuestion;

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    private Integer aiScore;

    @Enumerated(EnumType.STRING)
    private Status status = Status.IN_PROGRESS;

    @OneToMany(mappedBy = "mockInterview", cascade = CascadeType.ALL)
    private List<MockAnswer> answers;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    public enum Status {
        IN_PROGRESS, COMPLETED
    }

    // ── Constructors ──────────────────────────────────────────────────────────
    public MockInterview() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTargetCompany() { return targetCompany; }
    public void setTargetCompany(String targetCompany) { this.targetCompany = targetCompany; }

    public Question.Category getCategory() { return category; }
    public void setCategory(Question.Category category) { this.category = category; }

    public Question.Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Question.Difficulty difficulty) { this.difficulty = difficulty; }

    public String getAiGeneratedQuestion() { return aiGeneratedQuestion; }
    public void setAiGeneratedQuestion(String aiGeneratedQuestion) { this.aiGeneratedQuestion = aiGeneratedQuestion; }

    public String getAiFeedback() { return aiFeedback; }
    public void setAiFeedback(String aiFeedback) { this.aiFeedback = aiFeedback; }

    public Integer getAiScore() { return aiScore; }
    public void setAiScore(Integer aiScore) { this.aiScore = aiScore; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<MockAnswer> getAnswers() { return answers; }
    public void setAnswers(List<MockAnswer> answers) { this.answers = answers; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final MockInterview obj = new MockInterview();

        public Builder user(User v) { obj.user = v; return this; }
        public Builder targetCompany(String v) { obj.targetCompany = v; return this; }
        public Builder category(Question.Category v) { obj.category = v; return this; }
        public Builder difficulty(Question.Difficulty v) { obj.difficulty = v; return this; }
        public Builder aiGeneratedQuestion(String v) { obj.aiGeneratedQuestion = v; return this; }
        public Builder aiFeedback(String v) { obj.aiFeedback = v; return this; }
        public Builder aiScore(Integer v) { obj.aiScore = v; return this; }
        public Builder status(Status v) { obj.status = v; return this; }
        public MockInterview build() { return obj; }
    }
}