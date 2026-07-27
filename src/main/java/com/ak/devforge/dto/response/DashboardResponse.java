package com.ak.devforge.dto.response;
import java.util.List;
public class DashboardResponse {
    private String fullName, username, targetCompany;
    private Integer leetcodeRating, leetcodeAcceptanceRate, leetcodeStreak;
    private String leetcodeRank;
    private Integer currentStreak, longestStreak;
    private Integer totalInterviews, completedInterviews;
    private Double averageInterviewScore;
    private Integer atsScore;
    private Boolean hasResume;
    private Integer totalSavedQuestions, readinessScore;
    private String readinessLevel, studyPlan;
    private List<String> weakTopics;
    public DashboardResponse() {}
    public String getFullName() { return fullName; } public void setFullName(String v) { this.fullName = v; }
    public String getUsername() { return username; } public void setUsername(String v) { this.username = v; }
    public String getTargetCompany() { return targetCompany; } public void setTargetCompany(String v) { this.targetCompany = v; }
    public Integer getLeetcodeAcceptanceRate() { return leetcodeAcceptanceRate; } public void setLeetcodeAcceptanceRate(Integer v) { this.leetcodeAcceptanceRate = v; }
    public Integer getLeetcodeStreak() { return leetcodeStreak; } public void setLeetcodeStreak(Integer v) { this.leetcodeStreak = v; }
    public Integer getLeetcodeRating() { return leetcodeRating; } public void setLeetcodeRating(Integer v) { this.leetcodeRating = v; }
    public String getLeetcodeRank() { return leetcodeRank; } public void setLeetcodeRank(String v) { this.leetcodeRank = v; }
    public Integer getCurrentStreak() { return currentStreak; } public void setCurrentStreak(Integer v) { this.currentStreak = v; }
    public Integer getLongestStreak() { return longestStreak; } public void setLongestStreak(Integer v) { this.longestStreak = v; }
    public Integer getTotalInterviews() { return totalInterviews; } public void setTotalInterviews(Integer v) { this.totalInterviews = v; }
    public Integer getCompletedInterviews() { return completedInterviews; } public void setCompletedInterviews(Integer v) { this.completedInterviews = v; }
    public Double getAverageInterviewScore() { return averageInterviewScore; } public void setAverageInterviewScore(Double v) { this.averageInterviewScore = v; }
    public Integer getAtsScore() { return atsScore; } public void setAtsScore(Integer v) { this.atsScore = v; }
    public Boolean getHasResume() { return hasResume; } public void setHasResume(Boolean v) { this.hasResume = v; }
    public Integer getTotalSavedQuestions() { return totalSavedQuestions; } public void setTotalSavedQuestions(Integer v) { this.totalSavedQuestions = v; }
    public Integer getReadinessScore() { return readinessScore; } public void setReadinessScore(Integer v) { this.readinessScore = v; }
    public String getReadinessLevel() { return readinessLevel; } public void setReadinessLevel(String v) { this.readinessLevel = v; }
    public String getStudyPlan() { return studyPlan; } public void setStudyPlan(String v) { this.studyPlan = v; }
    public List<String> getWeakTopics() { return weakTopics; } public void setWeakTopics(List<String> v) { this.weakTopics = v; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final DashboardResponse o = new DashboardResponse();
        public Builder fullName(String v) { o.fullName = v; return this; }
        public Builder username(String v) { o.username = v; return this; }
        public Builder targetCompany(String v) { o.targetCompany = v; return this; }
        public Builder leetcodeAcceptanceRate(Integer v) { o.leetcodeAcceptanceRate = v; return this; }
        public Builder leetcodeStreak(Integer v) { o.leetcodeStreak = v; return this; }
        public Builder leetcodeRating(Integer v) { o.leetcodeRating = v; return this; }
        public Builder leetcodeRank(String v) { o.leetcodeRank = v; return this; }
        public Builder currentStreak(Integer v) { o.currentStreak = v; return this; }
        public Builder longestStreak(Integer v) { o.longestStreak = v; return this; }
        public Builder totalInterviews(Integer v) { o.totalInterviews = v; return this; }
        public Builder completedInterviews(Integer v) { o.completedInterviews = v; return this; }
        public Builder averageInterviewScore(Double v) { o.averageInterviewScore = v; return this; }
        public Builder atsScore(Integer v) { o.atsScore = v; return this; }
        public Builder hasResume(Boolean v) { o.hasResume = v; return this; }
        public Builder totalSavedQuestions(Integer v) { o.totalSavedQuestions = v; return this; }
        public Builder readinessScore(Integer v) { o.readinessScore = v; return this; }
        public Builder readinessLevel(String v) { o.readinessLevel = v; return this; }
        public Builder studyPlan(String v) { o.studyPlan = v; return this; }
        public Builder weakTopics(List<String> v) { o.weakTopics = v; return this; }
        public DashboardResponse build() { return o; }
    }
}