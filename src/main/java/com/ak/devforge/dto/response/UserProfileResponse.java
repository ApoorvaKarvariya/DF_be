package com.ak.devforge.dto.response;
public class UserProfileResponse {
    private Long id;
    private String fullName, email, username, college, bio, targetCompany;
    private String leetcodeHandle;
    private Integer leetcodeRating, leetcodeAcceptanceRate, leetcodeStreak, leetcodeTotalActiveDays;
    private String leetcodeRank;
    private Integer currentStreak, longestStreak, readinessScore, atsScore;
    private Boolean hasProfilePicture;
    private String githubHandle;
    public UserProfileResponse() {}
    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public String getFullName() { return fullName; }
    public void setFullName(String v) { this.fullName = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public String getUsername() { return username; }
    public void setUsername(String v) { this.username = v; }
    public String getCollege() { return college; }
    public void setCollege(String v) { this.college = v; }
    public String getBio() { return bio; }
    public void setBio(String v) { this.bio = v; }
    public String getTargetCompany() { return targetCompany; }
    public void setTargetCompany(String v) { this.targetCompany = v; }
    public String getLeetcodeHandle() { return leetcodeHandle; }
    public void setLeetcodeHandle(String v) { this.leetcodeHandle = v; }
    public Integer getLeetcodeRating() { return leetcodeRating; }
    public void setLeetcodeRating(Integer v) { this.leetcodeRating = v; }
    public String getLeetcodeRank() { return leetcodeRank; }
    public void setLeetcodeRank(String v) { this.leetcodeRank = v; }
    public Integer getLeetcodeAcceptanceRate() { return leetcodeAcceptanceRate; }
    public void setLeetcodeAcceptanceRate(Integer v) { this.leetcodeAcceptanceRate = v; }
    public Integer getLeetcodeStreak() { return leetcodeStreak; }
    public void setLeetcodeStreak(Integer v) { this.leetcodeStreak = v; }
    public Integer getLeetcodeTotalActiveDays() { return leetcodeTotalActiveDays; }
    public void setLeetcodeTotalActiveDays(Integer v) { this.leetcodeTotalActiveDays = v; }
    public Integer getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(Integer v) { this.currentStreak = v; }
    public Integer getLongestStreak() { return longestStreak; }
    public void setLongestStreak(Integer v) { this.longestStreak = v; }
    public Integer getReadinessScore() { return readinessScore; }
    public void setReadinessScore(Integer v) { this.readinessScore = v; }
    public Integer getAtsScore() { return atsScore; }
    public void setAtsScore(Integer v) { this.atsScore = v; }
    public Boolean getHasProfilePicture() { return hasProfilePicture; }
    public void setHasProfilePicture(Boolean v) { this.hasProfilePicture = v; }
    public String getGithubHandle() { return githubHandle; }
    public void setGithubHandle(String v) { this.githubHandle = v; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final UserProfileResponse o = new UserProfileResponse();
        public Builder id(Long v) { o.id = v; return this; }
        public Builder fullName(String v) { o.fullName = v; return this; }
        public Builder email(String v) { o.email = v; return this; }
        public Builder username(String v) { o.username = v; return this; }
        public Builder college(String v) { o.college = v; return this; }
        public Builder bio(String v) { o.bio = v; return this; }
        public Builder targetCompany(String v) { o.targetCompany = v; return this; }
        public Builder leetcodeHandle(String v) { o.leetcodeHandle = v; return this; }
        public Builder leetcodeRating(Integer v) { o.leetcodeRating = v; return this; }
        public Builder leetcodeRank(String v) { o.leetcodeRank = v; return this; }
        public Builder leetcodeAcceptanceRate(Integer v) { o.leetcodeAcceptanceRate = v; return this; }
        public Builder leetcodeStreak(Integer v) { o.leetcodeStreak = v; return this; }
        public Builder leetcodeTotalActiveDays(Integer v) { o.leetcodeTotalActiveDays = v; return this; }
        public Builder currentStreak(Integer v) { o.currentStreak = v; return this; }
        public Builder longestStreak(Integer v) { o.longestStreak = v; return this; }
        public Builder readinessScore(Integer v) { o.readinessScore = v; return this; }
        public Builder atsScore(Integer v) { o.atsScore = v; return this; }
        public Builder hasProfilePicture(Boolean v) { o.hasProfilePicture = v; return this; }
        public Builder githubHandle(String v) { o.githubHandle = v; return this; }
        public UserProfileResponse build() { return o; }
    }
}
