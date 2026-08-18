package com.toletboards.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.toletboards.dto.AuthResponse;
import com.toletboards.dto.GoogleLoginRequest;
import com.toletboards.dto.GoogleLoginResponse;
import com.toletboards.dto.LoginRequest;
import com.toletboards.dto.RefreshTokenRequest;
import com.toletboards.dto.RegisterRequest;
import com.toletboards.service.AuthService;
import com.toletboards.service.GoogleAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
 public class AuthController {

    private final AuthService authService;

     private final GoogleAuthService googleAuthService;

    /**
     * Register User / Agent
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    /**
     * Login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request));
    }

    /**
     * Refresh Access Token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(
                authService.refreshToken(request));
    }

    /**
     * Logout
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            Authentication authentication) {

        authService.logout(authentication.getName());

        return ResponseEntity.ok("Logged out successfully");
    }

@PostMapping("/google")
    public ResponseEntity<GoogleLoginResponse> googleLogin(
            @Valid @RequestBody GoogleLoginRequest request) {

        return ResponseEntity.ok(
                googleAuthService.login(request)
        );
    }


    /**
     * Complete Google Registration
     *
     * Used only after a new Google user
     * provides their phone number.
     */
    @PostMapping("/google/complete")
    public ResponseEntity<GoogleLoginResponse> completeGoogleRegistration(
            @Valid @RequestBody GoogleLoginRequest request) {

        return ResponseEntity.ok(
                googleAuthService.completeRegistration(request)
        );
    }

}