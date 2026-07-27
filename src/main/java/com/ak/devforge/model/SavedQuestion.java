package com.ak.devforge.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_questions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "question_id"}))
public class SavedQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @CreationTimestamp
    private LocalDateTime savedAt;

    public SavedQuestion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public LocalDateTime getSavedAt() { return savedAt; }
    public void setSavedAt(LocalDateTime savedAt) { this.savedAt = savedAt; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final SavedQuestion obj = new SavedQuestion();
        public Builder user(User v) { obj.user = v; return this; }
        public Builder question(Question v) { obj.question = v; return this; }
        public SavedQuestion build() { return obj; }
    }
}