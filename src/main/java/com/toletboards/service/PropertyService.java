package com.toletboards.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.toletboards.dto.PropertyRequest;
import com.toletboards.dto.PropertyResponse;



public interface PropertyService {

        

    PropertyResponse createProperty(
            PropertyRequest request,
            List<MultipartFile> images,
            UserDetails userDetails);

    List<PropertyResponse> getMyProperties(
            UserDetails userDetails);

    PropertyResponse getProperty(Long propertyId);

    List<PropertyResponse> getAllProperties();

 

}