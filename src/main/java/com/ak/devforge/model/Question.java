package com.ak.devforge.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    private String company;
    private String topic;

    @Column(columnDefinition = "TEXT")
    private String hints;

    @Column(columnDefinition = "TEXT")
    private String solutionApproach;

    private String leetcodeLink;
    private Integer frequency;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum Difficulty { EASY, MEDIUM, HARD }
    public enum Category { DSA, SYSTEM_DESIGN, HR, CS_FUNDAMENTALS, BEHAVIORAL }

    public Question() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Question obj = new Question();
        public Builder title(String v) { obj.title = v; return this; }
        public Builder description(String v) { obj.description = v; return this; }
        public Builder difficulty(Difficulty v) { obj.difficulty = v; return this; }
        public Builder category(Category v) { obj.category = v; return this; }
        public Builder company(String v) { obj.company = v; return this; }
        public Builder topic(String v) { obj.topic = v; return this; }
        public Builder hints(String v) { obj.hints = v; return this; }
        public Builder solutionApproach(String v) { obj.solutionApproach = v; return this; }
        public Builder leetcodeLink(String v) { obj.leetcodeLink = v; return this; }
        public Builder frequency(Integer v) { obj.frequency = v; return this; }
        public Question build() { return obj; }
    }
}