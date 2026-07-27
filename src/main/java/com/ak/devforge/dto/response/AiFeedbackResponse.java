package com.ak.devforge.dto.response;
public class AiFeedbackResponse {
    private String question;
    private String userAnswer;
    private String feedback;
    private Integer score;
    private String strengths;
    private String improvements;
    private String idealAnswer;
    public AiFeedbackResponse() {}
    public String getQuestion() { return question; }
    public void setQuestion(String v) { this.question = v; }
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String v) { this.userAnswer = v; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String v) { this.feedback = v; }
    public Integer getScore() { return score; }
    public void setScore(Integer v) { this.score = v; }
    public String getStrengths() { return strengths; }
    public void setStrengths(String v) { this.strengths = v; }
    public String getImprovements() { return improvements; }
    public void setImprovements(String v) { this.improvements = v; }
    public String getIdealAnswer() { return idealAnswer; }
    public void setIdealAnswer(String v) { this.idealAnswer = v; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final AiFeedbackResponse o = new AiFeedbackResponse();
        public Builder question(String v) { o.question = v; return this; }
        public Builder userAnswer(String v) { o.userAnswer = v; return this; }
        public Builder feedback(String v) { o.feedback = v; return this; }
        public Builder score(Integer v) { o.score = v; return this; }
        public Builder strengths(String v) { o.strengths = v; return this; }
        public Builder improvements(String v) { o.improvements = v; return this; }
        public Builder idealAnswer(String v) { o.idealAnswer = v; return this; }
        public AiFeedbackResponse build() { return o; }
    }
}