package com.toletboards.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "property_visits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Property Details
     */

    private Long propertyId;

    private String propertyName;

    /*
     * Owner Details
     */

    private Long ownerId;

    private String ownerName;

    private String ownerPhone;

    /*
     * Visitor Details
     */

    private Long userId;

    private String userName;

    private String userPhone;

    /*
     * Visit Schedule
     */

    private LocalDate visitDate;

    private LocalTime visitTime;

    /*
     * Status
     */

    @Builder.Default
    private String status = "PENDING";

    /*
     * Created Date
     */

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}