package com.ak.devforge.dto.response;
import java.util.List;
public class ResumeAnalysisResponse {
    private String extractedText;
    private List<String> extractedSkills;
    private Integer atsScore;
    private String strengths, weaknesses, suggestions, overallFeedback, resumePath;
    public ResumeAnalysisResponse() {}
    public String getExtractedText() { return extractedText; } public void setExtractedText(String v) { this.extractedText = v; }
    public List<String> getExtractedSkills() { return extractedSkills; } public void setExtractedSkills(List<String> v) { this.extractedSkills = v; }
    public Integer getAtsScore() { return atsScore; } public void setAtsScore(Integer v) { this.atsScore = v; }
    public String getStrengths() { return strengths; } public void setStrengths(String v) { this.strengths = v; }
    public String getWeaknesses() { return weaknesses; } public void setWeaknesses(String v) { this.weaknesses = v; }
    public String getSuggestions() { return suggestions; } public void setSuggestions(String v) { this.suggestions = v; }
    public String getOverallFeedback() { return overallFeedback; } public void setOverallFeedback(String v) { this.overallFeedback = v; }
    public String getResumePath() { return resumePath; } public void setResumePath(String v) { this.resumePath = v; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final ResumeAnalysisResponse o = new ResumeAnalysisResponse();
        public Builder extractedText(String v) { o.extractedText = v; return this; }
        public Builder extractedSkills(List<String> v) { o.extractedSkills = v; return this; }
        public Builder atsScore(Integer v) { o.atsScore = v; return this; }
        public Builder strengths(String v) { o.strengths = v; return this; }
        public Builder weaknesses(String v) { o.weaknesses = v; return this; }
        public Builder suggestions(String v) { o.suggestions = v; return this; }
        public Builder overallFeedback(String v) { o.overallFeedback = v; return this; }
        public Builder resumePath(String v) { o.resumePath = v; return this; }
        public ResumeAnalysisResponse build() { return o; }
    }
}