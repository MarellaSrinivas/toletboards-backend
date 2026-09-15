package com.toletboards.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toletboards.dto.PresenceRequest;
import com.toletboards.service.UserPresenceService;

@RestController
@RequestMapping("/api/tracking")
@CrossOrigin
public class UserPresenceController {

    private final UserPresenceService presenceService;

    public UserPresenceController(
            UserPresenceService presenceService
    ) {
        this.presenceService = presenceService;
    }

    @PostMapping("/presence")
    public ResponseEntity<?> updatePresence(
            @RequestBody PresenceRequest request
    ) {

        presenceService.updatePresence(request);

        return ResponseEntity.ok(
                "Presence updated"
        );
    }
}