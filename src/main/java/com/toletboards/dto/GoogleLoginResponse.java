package com.toletboards.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginResponse {

    private boolean phoneRequired;

    private String googleId;

    private String fullName;

    private String email;

    private String message;

    private AuthResponse authResponse;
}