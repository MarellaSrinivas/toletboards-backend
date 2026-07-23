package com.toletboards.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private String role;

    private String city;

    private String state;

    private String profileImage;

    private Boolean verified;

    private Integer activeProperties;

}