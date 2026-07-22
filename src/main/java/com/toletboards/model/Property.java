package com.toletboards.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Owner
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    /*
     * Basic
     */

    private String propertyType;

    private String propertyCategory;

    private String propertyName;

    private Double totalArea;

    /*
     * Configuration
     */

    private Integer bhk;

    private Integer bathrooms;

    private Integer floors;

    private Integer balconies;

    private String propertyAge;

    /*
     * Rent
     */

    private BigDecimal monthlyRent;

    private BigDecimal securityDeposit;

    private BigDecimal maintenanceCharges;

    private String propertyStatus;

    /*
     * Tenant
     */

    private String preferredTenant;

    private String furnishingStatus;

    private String foodPreference;

    private Boolean petsAllowed;

    private Boolean smokingAllowed;

    private Boolean alcoholAllowed;

    private Boolean availableImmediately;

    private LocalDate availableFrom;

    /*
     * Description
     */

    @Column(columnDefinition="TEXT")
    private String description;

    /*
     * Address
     */

    private String state;

    private String city;

    @Column(columnDefinition="TEXT")
    private String address;

    private Double latitude;

    private Double longitude;

    private String googleMapLink;

    /*
     * Status
     */

    @Builder.Default
    private Boolean approved=false;

    @Builder.Default
    private Boolean active=true;

    @Builder.Default
    private LocalDateTime createdAt=LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt=LocalDateTime.now();

    @PrePersist
    void prePersist(){

        createdAt=LocalDateTime.now();
        updatedAt=LocalDateTime.now();

    }

    @PreUpdate
    void preUpdate(){

        updatedAt=LocalDateTime.now();

    }

    @OneToMany(
        mappedBy = "property",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
)
private java.util.List<PropertyImage> images =
        new java.util.ArrayList<>();

}