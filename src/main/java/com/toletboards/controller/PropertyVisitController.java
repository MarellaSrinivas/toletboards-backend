package com.toletboards.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.toletboards.dto.VisitRequest;
import com.toletboards.dto.VisitResponse;
import com.toletboards.service.PropertyVisitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
@Validated
public class PropertyVisitController {

    private final PropertyVisitService propertyVisitService;

    /*
     * Schedule Visit
     */

    @PostMapping
    public ResponseEntity<VisitResponse> scheduleVisit(

            @Valid
            @RequestBody VisitRequest request,

            @AuthenticationPrincipal
            UserDetails userDetails) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(propertyVisitService.scheduleVisit(
                        request,
                        userDetails));
    }

    /*
     * Logged-in User Visits
     */

    @GetMapping("/my")

    public ResponseEntity<List<VisitResponse>> getMyVisits(

            @AuthenticationPrincipal
            UserDetails userDetails) {

        return ResponseEntity.ok(

                propertyVisitService.getMyVisits(
                        userDetails));
    }

    /*
     * Owner Visit Requests
     */

    @GetMapping("/owner")

    public ResponseEntity<List<VisitResponse>> getOwnerVisits(

            @AuthenticationPrincipal
            UserDetails userDetails) {

        return ResponseEntity.ok(

                propertyVisitService.getOwnerVisits(
                        userDetails));
    }

}