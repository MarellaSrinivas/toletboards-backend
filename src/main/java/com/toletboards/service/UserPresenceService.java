package com.toletboards.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.toletboards.dto.PresenceRequest;
import com.toletboards.model.UserPresence;
import com.toletboards.repository.UserPresenceRepository;

@Service
public class UserPresenceService {

    private final UserPresenceRepository repository;

    public UserPresenceService(
            UserPresenceRepository repository
    ) {
        this.repository = repository;
    }

    public void updatePresence(PresenceRequest request) {

        LocalDateTime now = LocalDateTime.now();

        UserPresence presence =
                repository.findByVisitorId(request.getVisitorId())
                        .orElseGet(UserPresence::new);

        presence.setVisitorId(request.getVisitorId());

        presence.setUserId(
                request.getUserId()
        );

        presence.setPlatform(
                request.getPlatform()
        );

        presence.setCurrentPage(
                request.getPage()
        );

        presence.setIsLoggedIn(
                request.getLoggedIn() != null
                        ? request.getLoggedIn()
                        : false
        );

        presence.setLastSeen(now);

        if (presence.getCreatedAt() == null) {
            presence.setCreatedAt(now);
        }

        repository.save(presence);
    }
}