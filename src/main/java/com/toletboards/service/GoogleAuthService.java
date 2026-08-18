package com.toletboards.service;

import com.toletboards.dto.AuthResponse;
import com.toletboards.dto.GoogleLoginRequest;
import com.toletboards.dto.GoogleLoginResponse;

public interface GoogleAuthService {

GoogleLoginResponse login(GoogleLoginRequest request);

    GoogleLoginResponse completeRegistration(
            GoogleLoginRequest request
    );

}