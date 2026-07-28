package com.toletboards.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toletboards.dto.AuthResponse;
import com.toletboards.dto.LoginRequest;
import com.toletboards.dto.RefreshTokenRequest;
import com.toletboards.dto.RegisterRequest;
import com.toletboards.model.RefreshToken;
import com.toletboards.model.Role;
import com.toletboards.model.User;
import com.toletboards.repository.UserRepository;
import com.toletboards.security.JwtService;
import com.toletboards.service.AuthService;
import com.toletboards.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    /**
     * REGISTER
     */
    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone already exists");
        }

        Role role = request.getRole() == null
                ? Role.ROLE_USER
                : request.getRole();

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .enabled(true)
                .verified(true)
                .build();

        user = userRepository.save(user);

        String accessToken =
                jwtService.generateAccessToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return buildResponse(
                user,
                accessToken,
                refreshToken.getToken());

    }

    /**
     * Common Response Builder
     */
    private AuthResponse buildResponse(
            User user,
            String accessToken,
            String refreshToken) {

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900L)
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();

    } 

@Override
public AuthResponse login(LoginRequest request) {

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    String accessToken = jwtService.generateAccessToken(user);

    RefreshToken refreshToken =
            refreshTokenService.createRefreshToken(user);

    return buildResponse(
            user,
            accessToken,
            refreshToken.getToken());

}
@Override
public AuthResponse refreshToken(
        RefreshTokenRequest request) {

    RefreshToken refreshToken =
            refreshTokenService.findByToken(
                    request.getRefreshToken());

    refreshTokenService.verifyExpiration(refreshToken);

    User user = refreshToken.getUser();

    String accessToken =
            jwtService.generateAccessToken(user);

    return buildResponse(
            user,
            accessToken,
            refreshToken.getToken());

}

 @Override
public void logout(String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    refreshTokenService.deleteByUser(user);

}

}