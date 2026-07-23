package com.toletboards.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.toletboards.dto.VisitRequest;
import com.toletboards.dto.VisitResponse;

public interface PropertyVisitService {

    /*
     * Schedule Visit
     */
    VisitResponse scheduleVisit(
            VisitRequest request,
            UserDetails userDetails);

    /*
     * Logged-in user's visits
     */
    List<VisitResponse> getMyVisits(
            UserDetails userDetails);

    /*
     * Property owner's visit requests
     */
    List<VisitResponse> getOwnerVisits(
            UserDetails userDetails);

}