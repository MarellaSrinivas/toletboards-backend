package com.toletboards.service.impl;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toletboards.dto.VisitRequest;
import com.toletboards.dto.VisitResponse;
import com.toletboards.model.Property;
import com.toletboards.model.PropertyVisit;
import com.toletboards.model.User;
import com.toletboards.repository.PropertyRepository;
import com.toletboards.repository.PropertyVisitRepository;
import com.toletboards.repository.UserRepository;
import com.toletboards.service.PropertyVisitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PropertyVisitServiceImpl
        implements PropertyVisitService {

    private final PropertyVisitRepository propertyVisitRepository;

    private final PropertyRepository propertyRepository;

    private final UserRepository userRepository;

    /*
     * Schedule Visit
     */

    @Override
    public VisitResponse scheduleVisit(
            VisitRequest request,
            UserDetails userDetails) {

        // Logged in user

        User visitor = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Property

        Property property = propertyRepository
                .findById(request.getPropertyId())
                .orElseThrow(() ->
                        new RuntimeException("Property not found"));

        // Owner

        User owner = property.getOwner();

        // Save Visit

        PropertyVisit visit = PropertyVisit.builder()

                // Property

                .propertyId(property.getId())
                .propertyName(property.getPropertyName())

                // Owner

                .ownerId(owner.getId())
                .ownerName(owner.getFullName())
                .ownerPhone(owner.getPhone())

                // Visitor

                .userId(visitor.getId())
                .userName(visitor.getFullName())
                .userPhone(visitor.getPhone())

                // Schedule

                .visitDate(request.getVisitDate())
                .visitTime(request.getVisitTime())

                .build();

        visit = propertyVisitRepository.save(visit);

        return VisitResponse.builder()

                .id(visit.getId())

                .propertyId(visit.getPropertyId())
                .propertyName(visit.getPropertyName())

                .ownerId(visit.getOwnerId())
                .ownerName(visit.getOwnerName())
                .ownerPhone(visit.getOwnerPhone())

                .userId(visit.getUserId())
                .userName(visit.getUserName())
                .userPhone(visit.getUserPhone())

                .visitDate(visit.getVisitDate())
                .visitTime(visit.getVisitTime())

                .status(visit.getStatus())

                .createdAt(visit.getCreatedAt())

                .build();
    }

    /*
     * Logged-in User Visits
     */

    @Override
    public List<VisitResponse> getMyVisits(
            UserDetails userDetails) {

        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return propertyVisitRepository
                .findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /*
     * Owner Visit Requests
     */

    @Override
    public List<VisitResponse> getOwnerVisits(
            UserDetails userDetails) {

        User owner = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return propertyVisitRepository
                .findByOwnerId(owner.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /*
     * Common Response Mapper
     */

    private VisitResponse mapToResponse(
            PropertyVisit visit) {

        return VisitResponse.builder()

                .id(visit.getId())

                .propertyId(visit.getPropertyId())
                .propertyName(visit.getPropertyName())

                .ownerId(visit.getOwnerId())
                .ownerName(visit.getOwnerName())
                .ownerPhone(visit.getOwnerPhone())

                .userId(visit.getUserId())
                .userName(visit.getUserName())
                .userPhone(visit.getUserPhone())

                .visitDate(visit.getVisitDate())
                .visitTime(visit.getVisitTime())

                .status(visit.getStatus())

                .createdAt(visit.getCreatedAt())

                .build();
    }

}