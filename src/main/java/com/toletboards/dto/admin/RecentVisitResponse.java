package com.toletboards.dto.admin;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentVisitResponse {

    private Long id;

    private String propertyName;

    private String visitorName;

    private LocalDate visitDate;

    private LocalTime visitTime;

    private String status;

}