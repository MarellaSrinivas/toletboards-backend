package com.toletboards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toletboards.model.Property;
import com.toletboards.model.User;

public interface PropertyRepository
        extends JpaRepository<Property, Long> {

    List<Property> findByOwner(User owner);
        List<Property> findByApprovedTrue();

    

}