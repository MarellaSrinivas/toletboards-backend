package com.toletboards.dto.admin;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentPropertyResponse {

    private Long id;

    private String propertyName;

    private String ownerName;

    private String propertyType;

    private String city;

    private String coverImage;

    private LocalDateTime createdAt;

}