package com.toletboards.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitResponse {

    private Long id;

    /*
     * Property
     */

    private Long propertyId;

    private String propertyName;

    /*
     * Owner
     */

    private Long ownerId;

    private String ownerName;

    private String ownerPhone;

    /*
     * Visitor
     */

    private Long userId;

    private String userName;

    private String userPhone;

    /*
     * Schedule
     */

    private LocalDate visitDate;

    private LocalTime visitTime;

    /*
     * Status
     */

    private String status;

    /*
     * Created
     */

    private LocalDateTime createdAt;

}