package com.ak.devforge.dto.request;
import com.ak.devforge.model.Question;
import jakarta.validation.constraints.NotNull;
public class MockInterviewRequest {
    private String targetCompany;
    @NotNull private Question.Category category;
    @NotNull private Question.Difficulty difficulty;
    public String getTargetCompany() { return targetCompany; }
    public void setTargetCompany(String targetCompany) { this.targetCompany = targetCompany; }
    public Question.Category getCategory() { return category; }
    public void setCategory(Question.Category category) { this.category = category; }
    public Question.Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Question.Difficulty difficulty) { this.difficulty = difficulty; }
}