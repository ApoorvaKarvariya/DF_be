package com.ak.devforge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String username;

    private String college;
    private String bio;

    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    private byte[] profilePicture;

    private String profilePictureContentType;

    @Column(unique = true)
    private String leetcodeHandle;

    private Integer leetcodeRating;
    private String leetcodeRank;
    private Integer leetcodeAcceptanceRate;
    private Integer leetcodeStreak;
    private Integer leetcodeTotalActiveDays;

    private String resumePath;
    private Integer atsScore;
    private Integer currentStreak;
    private Integer longestStreak;
    private LocalDateTime lastSolvedDate;
    private Integer readinessScore;
    private String targetCompany;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    private Boolean isActive = true;
    private Boolean isEmailVerified = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(unique = true)
    private String githubHandle;

    private Integer githubContributions;
    private Integer githubRepos;
    private String githubTopLanguage;
    private Integer githubScore;

    private Integer xpPoints = 0;
    private Integer levelNum = 1;

    @Column(unique = true)
    private String resetPasswordToken;

    private LocalDateTime resetPasswordTokenExpiry;

    public enum Role {
        USER, ADMIN
    }

    // ── Constructors ──────────────────────────────────────────────────────────
    public User() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public byte[] getProfilePicture() { return profilePicture; }
    public void setProfilePicture(byte[] profilePicture) { this.profilePicture = profilePicture; }

    public String getProfilePictureContentType() { return profilePictureContentType; }
    public void setProfilePictureContentType(String profilePictureContentType) { this.profilePictureContentType = profilePictureContentType; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getLeetcodeHandle() { return leetcodeHandle; }
    public void setLeetcodeHandle(String leetcodeHandle) { this.leetcodeHandle = leetcodeHandle; }

    public Integer getLeetcodeRating() { return leetcodeRating; }
    public void setLeetcodeRating(Integer leetcodeRating) { this.leetcodeRating = leetcodeRating; }

    public String getLeetcodeRank() { return leetcodeRank; }
    public void setLeetcodeRank(String leetcodeRank) { this.leetcodeRank = leetcodeRank; }

    public Integer getLeetcodeAcceptanceRate() { return leetcodeAcceptanceRate; }
    public void setLeetcodeAcceptanceRate(Integer leetcodeAcceptanceRate) { this.leetcodeAcceptanceRate = leetcodeAcceptanceRate; }

    public Integer getLeetcodeStreak() { return leetcodeStreak; }
    public void setLeetcodeStreak(Integer leetcodeStreak) { this.leetcodeStreak = leetcodeStreak; }

    public Integer getLeetcodeTotalActiveDays() { return leetcodeTotalActiveDays; }
    public void setLeetcodeTotalActiveDays(Integer leetcodeTotalActiveDays) { this.leetcodeTotalActiveDays = leetcodeTotalActiveDays; }

    public String getResumePath() { return resumePath; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }

    public Integer getAtsScore() { return atsScore; }
    public void setAtsScore(Integer atsScore) { this.atsScore = atsScore; }

    public Integer getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }

    public Integer getLongestStreak() { return longestStreak; }
    public void setLongestStreak(Integer longestStreak) { this.longestStreak = longestStreak; }

    public LocalDateTime getLastSolvedDate() { return lastSolvedDate; }
    public void setLastSolvedDate(LocalDateTime lastSolvedDate) { this.lastSolvedDate = lastSolvedDate; }

    public Integer getReadinessScore() { return readinessScore; }
    public void setReadinessScore(Integer readinessScore) { this.readinessScore = readinessScore; }

    public String getTargetCompany() { return targetCompany; }
    public void setTargetCompany(String targetCompany) { this.targetCompany = targetCompany; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsEmailVerified() { return isEmailVerified; }
    public void setIsEmailVerified(Boolean isEmailVerified) { this.isEmailVerified = isEmailVerified; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getGithubHandle() { return githubHandle; }
    public void setGithubHandle(String githubHandle) { this.githubHandle = githubHandle; }

    public Integer getGithubContributions() { return githubContributions; }
    public void setGithubContributions(Integer githubContributions) { this.githubContributions = githubContributions; }

    public Integer getGithubRepos() { return githubRepos; }
    public void setGithubRepos(Integer githubRepos) { this.githubRepos = githubRepos; }

    public String getGithubTopLanguage() { return githubTopLanguage; }
    public void setGithubTopLanguage(String githubTopLanguage) { this.githubTopLanguage = githubTopLanguage; }

    public Integer getGithubScore() { return githubScore; }
    public void setGithubScore(Integer githubScore) { this.githubScore = githubScore; }

    public Integer getXpPoints() { return xpPoints; }
    public void setXpPoints(Integer xpPoints) { this.xpPoints = xpPoints; }

    public Integer getLevelNum() { return levelNum; }
    public void setLevelNum(Integer levelNum) { this.levelNum = levelNum; }

    public String getResetPasswordToken() { return resetPasswordToken; }
    public void setResetPasswordToken(String resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }

    public LocalDateTime getResetPasswordTokenExpiry() { return resetPasswordTokenExpiry; }
    public void setResetPasswordTokenExpiry(LocalDateTime resetPasswordTokenExpiry) { this.resetPasswordTokenExpiry = resetPasswordTokenExpiry; }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final User user = new User();

        public Builder id(Long id) { user.id = id; return this; }
        public Builder fullName(String v) { user.fullName = v; return this; }
        public Builder email(String v) { user.email = v; return this; }
        public Builder password(String v) { user.password = v; return this; }
        public Builder username(String v) { user.username = v; return this; }
        public Builder college(String v) { user.college = v; return this; }
        public Builder bio(String v) { user.bio = v; return this; }
        public Builder targetCompany(String v) { user.targetCompany = v; return this; }
        public Builder leetcodeHandle(String v) { user.leetcodeHandle = v; return this; }
        public Builder leetcodeRating(Integer v) { user.leetcodeRating = v; return this; }
        public Builder leetcodeRank(String v) { user.leetcodeRank = v; return this; }
        public Builder leetcodeAcceptanceRate(Integer v) { user.leetcodeAcceptanceRate = v; return this; }
        public Builder leetcodeStreak(Integer v) { user.leetcodeStreak = v; return this; }
        public Builder leetcodeTotalActiveDays(Integer v) { user.leetcodeTotalActiveDays = v; return this; }
        public Builder profilePicture(byte[] v) { user.profilePicture = v; return this; }
        public Builder profilePictureContentType(String v) { user.profilePictureContentType = v; return this; }
        public Builder resumePath(String v) { user.resumePath = v; return this; }
        public Builder atsScore(Integer v) { user.atsScore = v; return this; }
        public Builder currentStreak(Integer v) { user.currentStreak = v; return this; }
        public Builder longestStreak(Integer v) { user.longestStreak = v; return this; }
        public Builder readinessScore(Integer v) { user.readinessScore = v; return this; }
        public Builder role(Role v) { user.role = v; return this; }
        public Builder isActive(Boolean v) { user.isActive = v; return this; }
        public Builder githubHandle(String v) { user.githubHandle = v; return this; }
        public Builder githubContributions(Integer v) { user.githubContributions = v; return this; }
        public Builder githubRepos(Integer v) { user.githubRepos = v; return this; }
        public Builder githubTopLanguage(String v) { user.githubTopLanguage = v; return this; }
        public Builder githubScore(Integer v) { user.githubScore = v; return this; }
        public Builder xpPoints(Integer v) { user.xpPoints = v; return this; }
        public Builder levelNum(Integer v) { user.levelNum = v; return this; }
        public User build() { return user; }
    }
}