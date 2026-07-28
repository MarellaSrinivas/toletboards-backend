

package com.toletboards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toletboards.dto.DashboardResponse;
import com.toletboards.dto.PropertyRequest;
import com.toletboards.dto.PropertyResponse;
import com.toletboards.service.PropertyService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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