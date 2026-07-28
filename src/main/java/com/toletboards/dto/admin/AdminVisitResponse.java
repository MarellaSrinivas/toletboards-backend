package com.toletboards.dto.admin;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminVisitResponse {

    private Long id;

    private String propertyName;

    private String ownerName;
    private String ownerPhone;


    private String visitorName;

    private String visitorPhone;

    private LocalDate visitDate;

    private LocalTime visitTime;

    private String status;

    private LocalDateTime createdAt;
}