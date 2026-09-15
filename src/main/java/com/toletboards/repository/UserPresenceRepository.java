package com.toletboards.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toletboards.model.UserPresence;

public interface UserPresenceRepository
        extends JpaRepository<UserPresence, Long> {

    Optional<UserPresence> findByVisitorId(String visitorId);

    long countByLastSeenAfter(LocalDateTime time);

    @Query("""
        SELECT COUNT(u)
        FROM UserPresence u
        WHERE u.lastSeen > :time
        AND u.isLoggedIn = :isLoggedIn
    """)
    long countByLastSeenAfterAndIsLoggedIn(
            @Param("time") LocalDateTime time,
            @Param("isLoggedIn") Boolean isLoggedIn
    );

    long countByLastSeenAfterAndCurrentPage(
            LocalDateTime time,
            String currentPage
    );
}