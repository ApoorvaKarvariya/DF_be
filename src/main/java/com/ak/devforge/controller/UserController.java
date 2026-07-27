package com.ak.devforge.controller;

import com.ak.devforge.dto.request.ConnectHandleRequest;
import com.ak.devforge.dto.request.UpdateProfileRequest;
import com.ak.devforge.dto.response.UserProfileResponse;
import com.ak.devforge.model.User;
import com.ak.devforge.service.UserService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users/me
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    // GET /api/users/{username}
    @GetMapping("/{username}")
    public ResponseEntity<UserProfileResponse> getUserByUsername(
            @PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    // PUT /api/users/me
    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }

    // POST /api/users/connect-handle
    @PostMapping("/connect-handle")
    public ResponseEntity<UserProfileResponse> connectHandle(
            @Valid @RequestBody ConnectHandleRequest request) {
        return ResponseEntity.ok(userService.connectHandle(request));
    }

    // POST /api/users/me/profile-picture  (multipart/form-data, field name "file")
    @PostMapping("/me/profile-picture")
    public ResponseEntity<UserProfileResponse> uploadProfilePicture(
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(
                userService.uploadProfilePicture(file.getBytes(), file.getContentType())
        );
    }

    // DELETE /api/users/me/profile-picture
    @DeleteMapping("/me/profile-picture")
    public ResponseEntity<UserProfileResponse> deleteProfilePicture() {
        return ResponseEntity.ok(userService.deleteProfilePicture());
    }

    // GET /api/users/{username}/profile-picture  (public - used directly in <img src>)
    @GetMapping("/{username}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String username) {
        User user = userService.getUserEntityByUsername(username);
        if (user.getProfilePicture() == null || user.getProfilePicture().length == 0) {
            return ResponseEntity.notFound().build();
        }
        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(user.getProfilePictureContentType());
        } catch (Exception e) {
            mediaType = MediaType.IMAGE_JPEG;
        }
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                .body(user.getProfilePicture());
    }
}