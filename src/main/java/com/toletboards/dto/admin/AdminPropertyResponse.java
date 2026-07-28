package com.toletboards.dto.admin;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPropertyResponse {

    private Long id;

    private String propertyName;

    private String ownerName;

    private String ownerPhone;

    private String propertyType;

    private String propertyCategory;

    private String city;

    private String state;

    private String coverImage;

    private String approvalStatus;

    private Boolean active;

    private LocalDateTime createdAt;

}