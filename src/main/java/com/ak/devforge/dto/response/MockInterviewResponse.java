package com.ak.devforge.dto.response;
import com.ak.devforge.model.MockInterview;
import com.ak.devforge.model.Question;
import java.time.LocalDateTime;
import java.util.List;
public class MockInterviewResponse {
    private Long id;
    private String targetCompany, aiGeneratedQuestion, aiFeedback;
    private Question.Category category;
    private Question.Difficulty difficulty;
    private Integer aiScore;
    private MockInterview.Status status;
    private List<AiFeedbackResponse> answers;
    private LocalDateTime createdAt, completedAt;
    public MockInterviewResponse() {}
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getTargetCompany() { return targetCompany; } public void setTargetCompany(String v) { this.targetCompany = v; }
    public String getAiGeneratedQuestion() { return aiGeneratedQuestion; } public void setAiGeneratedQuestion(String v) { this.aiGeneratedQuestion = v; }
    public String getAiFeedback() { return aiFeedback; } public void setAiFeedback(String v) { this.aiFeedback = v; }
    public Question.Category getCategory() { return category; } public void setCategory(Question.Category v) { this.category = v; }
    public Question.Difficulty getDifficulty() { return difficulty; } public void setDifficulty(Question.Difficulty v) { this.difficulty = v; }
    public Integer getAiScore() { return aiScore; } public void setAiScore(Integer v) { this.aiScore = v; }
    public MockInterview.Status getStatus() { return status; } public void setStatus(MockInterview.Status v) { this.status = v; }
    public List<AiFeedbackResponse> getAnswers() { return answers; } public void setAnswers(List<AiFeedbackResponse> v) { this.answers = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public LocalDateTime getCompletedAt() { return completedAt; } public void setCompletedAt(LocalDateTime v) { this.completedAt = v; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final MockInterviewResponse o = new MockInterviewResponse();
        public Builder id(Long v) { o.id = v; return this; }
        public Builder targetCompany(String v) { o.targetCompany = v; return this; }
        public Builder aiGeneratedQuestion(String v) { o.aiGeneratedQuestion = v; return this; }
        public Builder aiFeedback(String v) { o.aiFeedback = v; return this; }
        public Builder category(Question.Category v) { o.category = v; return this; }
        public Builder difficulty(Question.Difficulty v) { o.difficulty = v; return this; }
        public Builder aiScore(Integer v) { o.aiScore = v; return this; }
        public Builder status(MockInterview.Status v) { o.status = v; return this; }
        public Builder answers(List<AiFeedbackResponse> v) { o.answers = v; return this; }
        public Builder createdAt(LocalDateTime v) { o.createdAt = v; return this; }
        public Builder completedAt(LocalDateTime v) { o.completedAt = v; return this; }
        public MockInterviewResponse build() { return o; }
    }
}