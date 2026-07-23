package com.toletboards.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VisitRequest {

    @NotNull(message = "Property Id is required")
    private Long propertyId;

    @NotNull(message = "Visit Date is required")
    private LocalDate visitDate;

    @NotNull(message = "Visit Time is required")
    private LocalTime visitTime;

}