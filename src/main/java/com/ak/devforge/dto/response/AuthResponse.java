package com.ak.devforge.dto.response;
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long userId;
    private String email;
    private String username;
    private String fullName;
    private String role;
    public AuthResponse() {}
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String v) { this.accessToken = v; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String v) { this.refreshToken = v; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String v) { this.tokenType = v; }
    public Long getUserId() { return userId; }
    public void setUserId(Long v) { this.userId = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public String getUsername() { return username; }
    public void setUsername(String v) { this.username = v; }
    public String getFullName() { return fullName; }
    public void setFullName(String v) { this.fullName = v; }
    public String getRole() { return role; }
    public void setRole(String v) { this.role = v; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final AuthResponse o = new AuthResponse();
        public Builder accessToken(String v) { o.accessToken = v; return this; }
        public Builder refreshToken(String v) { o.refreshToken = v; return this; }
        public Builder tokenType(String v) { o.tokenType = v; return this; }
        public Builder userId(Long v) { o.userId = v; return this; }
        public Builder email(String v) { o.email = v; return this; }
        public Builder username(String v) { o.username = v; return this; }
        public Builder fullName(String v) { o.fullName = v; return this; }
        public Builder role(String v) { o.role = v; return this; }
        public AuthResponse build() { return o; }
    }
}