package com.toletboards.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {

    private Long id;

    // Owner
    private Long ownerId;
    private String ownerName;

    // Basic
    private String propertyType;
    private String propertyCategory;
    private String propertyName;

    private Double totalArea;

    // Configuration
    private Integer bhk;
    private Integer bathrooms;
    private Integer floors;
    private Integer balconies;
    private String propertyAge;

    // Rent
    private BigDecimal monthlyRent;
    private BigDecimal securityDeposit;
    private BigDecimal maintenanceCharges;

    private String propertyStatus;

    // Tenant
    private String preferredTenant;
    private String furnishingStatus;
    private String foodPreference;

    private Boolean petsAllowed;
    private Boolean smokingAllowed;
    private Boolean alcoholAllowed;

    // Description
    private String description;

    // Address
    private String state;
    private String city;
    private String address;

    private Double latitude;
    private Double longitude;
    private String googleMapLink;

    // Images
    private String coverImage;
    private List<String> imageUrls;

    private Boolean approved;
}