package com.toletboards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toletboards.model.Property;
import com.toletboards.model.User;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; 
import com.toletboards.model.PropertyApprovalStatus;


public interface PropertyRepository
        extends JpaRepository<Property, Long> {

    List<Property> findByOwner(User owner);
        List<Property> findByApprovalStatus(PropertyApprovalStatus status);

 

    long countByOwner(User owner);

    long countByOwnerAndActiveTrue(User owner);

    long countByOwnerAndApprovalStatus(
        User owner,
        PropertyApprovalStatus status);
     



    long count();

long countByApprovalStatus(PropertyApprovalStatus approvalStatus);

List<Property> findTop5ByOrderByCreatedAtDesc();

    
Page<Property> findAll(Pageable pageable);

Page<Property> findByPropertyNameContainingIgnoreCaseOrCityContainingIgnoreCase(

        String propertyName,
        String city,
        Pageable pageable);


        Page<Property> findByApprovalStatus(
        PropertyApprovalStatus status,
        Pageable pageable);


        Page<Property> findByPropertyType(
        String propertyType,
        Pageable pageable);


        Page<Property> findByApprovalStatusAndPropertyNameContainingIgnoreCaseOrApprovalStatusAndCityContainingIgnoreCase(

        PropertyApprovalStatus status,
        String propertyName,

        PropertyApprovalStatus status2,
        String city,

        Pageable pageable);



        List<Property> findByActiveTrue();

Page<Property> findByActiveTrue(Pageable pageable);

long countByActiveTrue();

long countByActiveFalse();

}