package com.ak.devforge.service;

import com.ak.devforge.dto.request.ConnectHandleRequest;
import com.ak.devforge.dto.request.UpdateProfileRequest;
import com.ak.devforge.dto.response.UserProfileResponse;
import com.ak.devforge.exception.BadRequestException;
import com.ak.devforge.exception.ResourceNotFoundException;
import com.ak.devforge.model.User;
import com.ak.devforge.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get currently logged in user
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // Get my profile
    public UserProfileResponse getMyProfile() {
        return mapToResponse(getCurrentUser());
    }

    // Get any user by username
    public UserProfileResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + username));
        return mapToResponse(user);
    }

    // Update profile
    @Transactional
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();

        if (request.getFullName() != null)
            user.setFullName(request.getFullName());
        if (request.getBio() != null)
            user.setBio(request.getBio());
        if (request.getCollege() != null)
            user.setCollege(request.getCollege());
        if (request.getTargetCompany() != null)
            user.setTargetCompany(request.getTargetCompany());

        userRepository.save(user);
        return mapToResponse(user);
    }

    // Connect CF, LC or GitHub handle
    @Transactional
    public UserProfileResponse connectHandle(ConnectHandleRequest request) {
        User user = getCurrentUser();
        String platform = request.getPlatform().toUpperCase();
        String handle = request.getHandle().trim();

        switch (platform) {
            case "LEETCODE" -> {
                user.setLeetcodeHandle(handle);
            }
            case "GITHUB" -> {
                user.setGithubHandle(handle);
            }
            default -> throw new BadRequestException(
                    "Invalid platform. Use LEETCODE or GITHUB"
            );
        }

        userRepository.save(user);
        return mapToResponse(user);
    }

    // Upload / replace profile picture (stored as BLOB in MySQL)
    @Transactional
    public UserProfileResponse uploadProfilePicture(byte[] imageBytes, String contentType) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new BadRequestException("Empty file");
        }
        if (imageBytes.length > 3 * 1024 * 1024) {
            throw new BadRequestException("Image too large. Max 3MB allowed.");
        }
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Only image files are allowed");
        }

        User user = getCurrentUser();
        user.setProfilePicture(imageBytes);
        user.setProfilePictureContentType(contentType);
        userRepository.save(user);
        return mapToResponse(user);
    }

    // Remove profile picture
    @Transactional
    public UserProfileResponse deleteProfilePicture() {
        User user = getCurrentUser();
        user.setProfilePicture(null);
        user.setProfilePictureContentType(null);
        userRepository.save(user);
        return mapToResponse(user);
    }

    // Fetch raw profile picture bytes for a username (public, used in <img src>)
    public User getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    // Map User entity to response DTO
    private UserProfileResponse mapToResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .college(user.getCollege())
                .bio(user.getBio())
                .targetCompany(user.getTargetCompany())
                .leetcodeHandle(user.getLeetcodeHandle())
                .leetcodeRating(user.getLeetcodeRating())
                .leetcodeRank(user.getLeetcodeRank())
                .leetcodeAcceptanceRate(user.getLeetcodeAcceptanceRate())
                .leetcodeStreak(user.getLeetcodeStreak())
                .leetcodeTotalActiveDays(user.getLeetcodeTotalActiveDays())
                .currentStreak(user.getCurrentStreak())
                .longestStreak(user.getLongestStreak())
                .readinessScore(user.getReadinessScore())
                .atsScore(user.getAtsScore())
                .hasProfilePicture(user.getProfilePicture() != null && user.getProfilePicture().length > 0)
                .githubHandle(user.getGithubHandle())
                .build();
    }
}