package com.ak.devforge.dto.response;
import com.ak.devforge.model.Question;
import java.time.LocalDateTime;
public class QuestionResponse {
    private Long id;
    private String title, description, company, topic, hints, solutionApproach, leetcodeLink;
    private Question.Difficulty difficulty;
    private Question.Category category;
    private Integer frequency;
    private Boolean isSaved;
    private LocalDateTime createdAt;
    public QuestionResponse() {}
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public String getCompany() { return company; } public void setCompany(String v) { this.company = v; }
    public String getTopic() { return topic; } public void setTopic(String v) { this.topic = v; }
    public String getHints() { return hints; } public void setHints(String v) { this.hints = v; }
    public String getSolutionApproach() { return solutionApproach; } public void setSolutionApproach(String v) { this.solutionApproach = v; }
    public String getLeetcodeLink() { return leetcodeLink; } public void setLeetcodeLink(String v) { this.leetcodeLink = v; }
    public Question.Difficulty getDifficulty() { return difficulty; } public void setDifficulty(Question.Difficulty v) { this.difficulty = v; }
    public Question.Category getCategory() { return category; } public void setCategory(Question.Category v) { this.category = v; }
    public Integer getFrequency() { return frequency; } public void setFrequency(Integer v) { this.frequency = v; }
    public Boolean getIsSaved() { return isSaved; } public void setIsSaved(Boolean v) { this.isSaved = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final QuestionResponse o = new QuestionResponse();
        public Builder id(Long v) { o.id = v; return this; }
        public Builder title(String v) { o.title = v; return this; }
        public Builder description(String v) { o.description = v; return this; }
        public Builder difficulty(Question.Difficulty v) { o.difficulty = v; return this; }
        public Builder category(Question.Category v) { o.category = v; return this; }
        public Builder company(String v) { o.company = v; return this; }
        public Builder topic(String v) { o.topic = v; return this; }
        public Builder hints(String v) { o.hints = v; return this; }
        public Builder solutionApproach(String v) { o.solutionApproach = v; return this; }
        public Builder leetcodeLink(String v) { o.leetcodeLink = v; return this; }
        public Builder frequency(Integer v) { o.frequency = v; return this; }
        public Builder isSaved(Boolean v) { o.isSaved = v; return this; }
        public Builder createdAt(LocalDateTime v) { o.createdAt = v; return this; }
        public QuestionResponse build() { return o; }
    }
}