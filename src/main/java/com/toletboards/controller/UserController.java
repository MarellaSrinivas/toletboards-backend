package com.toletboards.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.toletboards.dto.UpdateProfileRequest;
import com.toletboards.dto.UserProfileResponse;
import com.toletboards.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(

            @AuthenticationPrincipal
            UserDetails userDetails) {

        return ResponseEntity.ok(

                userService.getProfile(userDetails));

    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(

            @Valid
            @RequestBody
            UpdateProfileRequest request,

            @AuthenticationPrincipal
            UserDetails userDetails) {

        return ResponseEntity.ok(

                userService.updateProfile(

                        request,

                        userDetails));

    }

    @PostMapping("/profile/image")
    public ResponseEntity<UserProfileResponse> uploadImage(

            @RequestParam MultipartFile image,

            @AuthenticationPrincipal
            UserDetails userDetails) {

        return ResponseEntity.ok(

                userService.uploadProfileImage(

                        image,

                        userDetails));

    }

}