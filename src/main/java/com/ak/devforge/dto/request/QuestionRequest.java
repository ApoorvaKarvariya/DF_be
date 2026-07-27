package com.ak.devforge.dto.request;
import com.ak.devforge.model.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class QuestionRequest {
    @NotBlank private String title;
    private String description;
    @NotNull private Question.Difficulty difficulty;
    @NotNull private Question.Category category;
    private String company;
    private String topic;
    private String hints;
    private String solutionApproach;
    private String leetcodeLink;
    private Integer frequency;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Question.Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Question.Difficulty difficulty) { this.difficulty = difficulty; }
    public Question.Category getCategory() { return category; }
    public void setCategory(Question.Category category) { this.category = category; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getHints() { return hints; }
    public void setHints(String hints) { this.hints = hints; }
    public String getSolutionApproach() { return solutionApproach; }
    public void setSolutionApproach(String solutionApproach) { this.solutionApproach = solutionApproach; }
    public String getLeetcodeLink() { return leetcodeLink; }
    public void setLeetcodeLink(String leetcodeLink) { this.leetcodeLink = leetcodeLink; }
    public Integer getFrequency() { return frequency; }
    public void setFrequency(Integer frequency) { this.frequency = frequency; }
}