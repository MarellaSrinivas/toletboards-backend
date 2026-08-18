package com.toletboards.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.toletboards.dto.AuthResponse;
import com.toletboards.dto.GoogleLoginRequest;
import com.toletboards.dto.GoogleLoginResponse;
import com.toletboards.model.RefreshToken;
import com.toletboards.model.Role;
import com.toletboards.model.User;
import com.toletboards.repository.UserRepository;
import com.toletboards.security.JwtService;
import com.toletboards.service.GoogleAuthService;
import com.toletboards.service.GoogleTokenService;
import com.toletboards.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final GoogleTokenService googleTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    // =========================================================
    // GOOGLE LOGIN
    // =========================================================

    @Override
    public GoogleLoginResponse login(
            GoogleLoginRequest request) {

        try {

            GoogleIdToken.Payload payload =
                    googleTokenService.verify(
                            request.getCredential()
                    );

            String googleId =
                    payload.getSubject();

            String email =
                    payload.getEmail();

            String fullName =
                    (String) payload.get("name");


            if (email == null || email.isBlank()) {

                throw new RuntimeException(
                        "Google account email not available"
                );
            }


            User user =
                    userRepository
                            .findByEmail(email)
                            .orElse(null);


            // =================================================
            // NEW GOOGLE USER
            // =================================================

            if (user == null) {

                return GoogleLoginResponse.builder()
                        .phoneRequired(true)
                        .googleId(googleId)
                        .fullName(
                                fullName != null
                                        ? fullName
                                        : email.split("@")[0]
                        )
                        .email(email)
                        .message(
                                "Phone number is required"
                        )
                        .build();
            }


            // =================================================
            // EXISTING USER
            // =================================================

            AuthResponse authResponse =
                    createAuthResponse(user);


            return GoogleLoginResponse.builder()
                    .phoneRequired(false)
                    .googleId(googleId)
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .authResponse(authResponse)
                    .build();


        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Google login failed: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // COMPLETE GOOGLE REGISTRATION
    // =========================================================

    @Override
    public GoogleLoginResponse completeRegistration(
            GoogleLoginRequest request) {

        try {

            GoogleIdToken.Payload payload =
                    googleTokenService.verify(
                            request.getCredential()
                    );


            String email =
                    payload.getEmail();

            String fullName =
                    (String) payload.get("name");


            if (email == null || email.isBlank()) {

                throw new RuntimeException(
                        "Google account email not available"
                );
            }


            // =================================================
            // PHONE VALIDATION
            // =================================================

            if (request.getPhone() == null ||
                request.getPhone().trim().isEmpty()) {

                throw new RuntimeException(
                        "Phone number is required"
                );
            }


            String phone =
                    request.getPhone().trim();


            if (!phone.matches("\\d{10}")) {

                throw new RuntimeException(
                        "Please enter a valid 10-digit phone number"
                );
            }


            // =================================================
            // CHECK EMAIL
            // =================================================

            User existingUser =
                    userRepository
                            .findByEmail(email)
                            .orElse(null);


            if (existingUser != null) {

                AuthResponse authResponse =
                        createAuthResponse(existingUser);

                return GoogleLoginResponse.builder()
                        .phoneRequired(false)
                        .fullName(existingUser.getFullName())
                        .email(existingUser.getEmail())
                        .authResponse(authResponse)
                        .build();
            }


            // =================================================
            // CHECK PHONE
            // =================================================

            if (userRepository.existsByPhone(phone)) {

                throw new RuntimeException(
                        "Phone number already exists"
                );
            }


            // =================================================
            // CREATE USER
            // =================================================

            User user =
                    User.builder()
                            .fullName(
                                    fullName != null
                                            ? fullName
                                            : email.split("@")[0]
                            )
                            .email(email)
                            .phone(phone)
                            .password(
                                    passwordEncoder.encode(
                                            java.util.UUID
                                                    .randomUUID()
                                                    .toString()
                                    )
                            )
                            .role(Role.ROLE_USER)
                            .enabled(true)
                            .verified(true)
                            .build();


            user =
                    userRepository.save(user);


            // =================================================
            // CREATE AUTH RESPONSE
            // =================================================

            AuthResponse authResponse =
                    createAuthResponse(user);


            return GoogleLoginResponse.builder()
                    .phoneRequired(false)
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .authResponse(authResponse)
                    .build();


        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    e.getMessage()
            );
        }
    }


    // =========================================================
    // COMMON AUTH RESPONSE
    // =========================================================

    private AuthResponse createAuthResponse(
            User user) {

        String accessToken =
                jwtService.generateAccessToken(user);


        RefreshToken refreshToken =
                refreshTokenService
                        .createRefreshToken(user);


        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(
                        refreshToken.getToken()
                )
                .tokenType("Bearer")
                .expiresIn(900L)
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}