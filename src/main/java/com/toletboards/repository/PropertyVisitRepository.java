package com.toletboards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toletboards.model.PropertyVisit;

public interface PropertyVisitRepository
        extends JpaRepository<PropertyVisit, Long> {

    /*
     * Visits requested by a user
     */

    List<PropertyVisit> findByUserId(Long userId);

    /*
     * Visits received by an owner
     */

    List<PropertyVisit> findByOwnerId(Long ownerId);

    /*
     * Visits for a property
     */

    List<PropertyVisit> findByPropertyId(Long propertyId);

        long countByOwnerId(Long ownerId);


}