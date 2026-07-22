package com.toletboards.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toletboards.model.Property;
import com.toletboards.model.PropertyImage;

public interface PropertyImageRepository
        extends JpaRepository<PropertyImage, Long> {

    List<PropertyImage> findByProperty(Property property);

    Optional<PropertyImage> findByPropertyAndCoverImageTrue(Property property);

    void deleteByProperty(Property property);

}