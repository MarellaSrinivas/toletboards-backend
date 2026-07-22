package com.toletboards.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class PropertyRequest {

    private String propertyType;
    private String propertyCategory;
    private String propertyName;
    private Double totalArea;

    private Integer bhk;
    private Integer bathrooms;
    private Integer floors;
    private Integer balconies;
    private String propertyAge;

    private BigDecimal monthlyRent;
    private BigDecimal securityDeposit;
    private BigDecimal maintenanceCharges;
    private String propertyStatus;

    private String preferredTenant;
    private String furnishingStatus;
    private String foodPreference;

    private Boolean petsAllowed;
    private Boolean smokingAllowed;
    private Boolean alcoholAllowed;
    private Boolean availableImmediately;

    private LocalDate availableFrom;

    private String description;

    private String state;
    private String city;
    private String address;

    private Double latitude;
    private Double longitude;

    private String googleMapLink;

}