package com.ak.devforge.dto.request;
import jakarta.validation.constraints.NotBlank;
public class ConnectHandleRequest {
    @NotBlank private String handle;
    @NotBlank private String platform;
    public String getHandle() { return handle; }
    public void setHandle(String handle) { this.handle = handle; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
}