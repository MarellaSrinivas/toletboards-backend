package com.toletboards.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String phone;

    private String city;

    private String state;

}