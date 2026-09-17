

package com.toletboards.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toletboards.dto.DashboardResponse;
import com.toletboards.dto.PropertyRequest;
import com.toletboards.dto.PropertyResponse;
import com.toletboards.service.PropertyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    private final ObjectMapper objectMapper;

    /**
     * Upload Property
     */
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<PropertyResponse> uploadProperty(

            @RequestPart("property") String propertyJson,

            @RequestPart(value = "images", required = false)
            List<MultipartFile> images,

            @AuthenticationPrincipal UserDetails userDetails

    ) throws Exception {

        PropertyRequest request =
                objectMapper.readValue(
                        propertyJson,
                        PropertyRequest.class
                );

        return ResponseEntity.ok(

                propertyService.createProperty(
                        request,
                        images,
                        userDetails
                )

        );

    }

    /**
     * Public Home Page
     */
    @GetMapping
    public ResponseEntity<List<PropertyResponse>> getAllProperties() {

        return ResponseEntity.ok(

                propertyService.getAllProperties()

        );

    }


    @GetMapping("/category/{propertyCategory}")
public ResponseEntity<List<PropertyResponse>> getPropertiesByCategory(
        @PathVariable String propertyCategory) {

    return ResponseEntity.ok(
            propertyService.getPropertiesByCategory(
                    propertyCategory
            )
    );
}

    /**
     * Property Details
     */
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getProperty(
            @PathVariable Long id) {

        return ResponseEntity.ok(

                propertyService.getProperty(id)

        );

    }

    /**
     * Logged-in User Properties
     */
    @GetMapping("/my")
    public ResponseEntity<List<PropertyResponse>> getMyProperties(

            @AuthenticationPrincipal UserDetails userDetails

    ) {

        return ResponseEntity.ok(

                propertyService.getMyProperties(userDetails)

        );

    }

@GetMapping("/dashboard")
public ResponseEntity<DashboardResponse> dashboard(
        @AuthenticationPrincipal UserDetails userDetails) {

    return ResponseEntity.ok(
            propertyService.getDashboard(userDetails));
}

}