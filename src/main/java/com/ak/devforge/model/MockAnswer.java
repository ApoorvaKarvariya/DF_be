package com.ak.devforge.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "mock_answers")
public class MockAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mock_interview_id", nullable = false)
    private MockInterview mockInterview;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String userAnswer;

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    private Integer aiScore;

    @CreationTimestamp
    private LocalDateTime answeredAt;

    public MockAnswer() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public MockInterview getMockInterview() { return mockInterview; }
    public void setMockInterview(MockInterview mockInterview) { this.mockInterview = mockInterview; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    public String getAiFeedback() { return aiFeedback; }
    public void setAiFeedback(String aiFeedback) { this.aiFeedback = aiFeedback; }
    public Integer getAiScore() { return aiScore; }
    public void setAiScore(Integer aiScore) { this.aiScore = aiScore; }
    public LocalDateTime getAnsweredAt() { return answeredAt; }
    public void setAnsweredAt(LocalDateTime answeredAt) { this.answeredAt = answeredAt; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final MockAnswer obj = new MockAnswer();
        public Builder mockInterview(MockInterview v) { obj.mockInterview = v; return this; }
        public Builder question(String v) { obj.question = v; return this; }
        public Builder userAnswer(String v) { obj.userAnswer = v; return this; }
        public Builder aiFeedback(String v) { obj.aiFeedback = v; return this; }
        public Builder aiScore(Integer v) { obj.aiScore = v; return this; }
        public MockAnswer build() { return obj; }
    }
}