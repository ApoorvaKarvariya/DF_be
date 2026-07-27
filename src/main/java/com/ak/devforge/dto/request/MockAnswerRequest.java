package com.ak.devforge.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class MockAnswerRequest {
    @NotNull private Long interviewId;
    @NotBlank private String question;
    @NotBlank private String userAnswer;
    public Long getInterviewId() { return interviewId; }
    public void setInterviewId(Long interviewId) { this.interviewId = interviewId; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
}