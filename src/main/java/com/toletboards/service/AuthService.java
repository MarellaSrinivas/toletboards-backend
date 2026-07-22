package com.toletboards.service;

import com.toletboards.dto.AuthResponse;
import com.toletboards.dto.LoginRequest;
import com.toletboards.dto.RefreshTokenRequest;
import com.toletboards.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(String email);

}