package com.toletboards.dto.admin;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserListResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private String role;

    private Boolean enabled;

    private Boolean verified;

    private LocalDateTime createdAt;

    private Integer totalProperties;

}