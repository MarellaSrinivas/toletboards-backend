package com.toletboards.controller;

import com.toletboards.dto.PropertyResponse;
import com.toletboards.dto.admin.AdminDashboardResponse;
import com.toletboards.dto.admin.AdminPropertyResponse;
import com.toletboards.dto.admin.AdminUserStatsResponse;
import com.toletboards.dto.admin.AdminVisitResponse;
import com.toletboards.dto.admin.UserListResponse;
import com.toletboards.service.AdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    /*
     * =====================================================
     * Dashboard
     * =====================================================
     */

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard() {

        return ResponseEntity.ok(
                adminService.getDashboard());

    }

    /*
     * =====================================================
     * User Management
     * =====================================================
     */

    @GetMapping("/users")
    public ResponseEntity<Page<UserListResponse>> getUsers(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            String role) {

        return ResponseEntity.ok(

                adminService.getUsers(
                        page,
                        size,
                        search,
                        role));

    }

    @GetMapping("/users/stats")
    public ResponseEntity<AdminUserStatsResponse> getUserStats() {

        return ResponseEntity.ok(
                adminService.getUserStats());

    }

    /*
     * =====================================================
     * Property Management
     * =====================================================
     */

    @GetMapping("/properties")
public ResponseEntity<Page<AdminPropertyResponse>> getProperties(

        @RequestParam(defaultValue = "0")
        int page,

        @RequestParam(defaultValue = "10")
        int size,

        @RequestParam(required = false)
        String search,

        @RequestParam(required = false)
        String status,

        @RequestParam(required = false)
        String propertyType) {

    return ResponseEntity.ok(

            adminService.getProperties(

                    page,

                    size,

                    search,

                    status,

                    propertyType));

}

    @PutMapping("/properties/{id}/approve")
    public ResponseEntity<PropertyResponse> approveProperty(

            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminService.approveProperty(id));

    }

    @PutMapping("/properties/{id}/reject")
    public ResponseEntity<PropertyResponse> rejectProperty(

            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminService.rejectProperty(id));

    }

    @DeleteMapping("/properties/{id}")
    public ResponseEntity<Void> deleteProperty(

            @PathVariable Long id) {

        adminService.deleteProperty(id);

        return ResponseEntity.noContent().build();

    }


    @GetMapping("/visits")
public ResponseEntity<Page<AdminVisitResponse>> getVisits(

        @RequestParam(defaultValue = "0") int page,

        @RequestParam(defaultValue = "10") int size,

        @RequestParam(required = false) String status) {

    return ResponseEntity.ok(
            adminService.getVisits(page, size, status));
}

}