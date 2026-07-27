package com.ak.devforge.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class RegisterRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;
    @Email @NotBlank(message = "Email is required")
    private String email;
    @NotBlank @Size(min = 3, max = 20)
    private String username;
    @NotBlank @Size(min = 6)
    private String password;
    private String college;
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }
}