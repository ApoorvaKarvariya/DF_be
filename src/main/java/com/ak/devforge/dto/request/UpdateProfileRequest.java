package com.ak.devforge.dto.request;
public class UpdateProfileRequest {
    private String fullName;
    private String bio;
    private String college;
    private String targetCompany;
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }
    public String getTargetCompany() { return targetCompany; }
    public void setTargetCompany(String targetCompany) { this.targetCompany = targetCompany; }
}