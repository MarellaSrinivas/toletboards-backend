package com.toletboards.service;

import com.toletboards.dto.PropertyResponse;
import com.toletboards.dto.admin.AdminDashboardResponse;
import com.toletboards.dto.admin.AdminUserStatsResponse;
import com.toletboards.dto.admin.AdminVisitResponse;
import com.toletboards.dto.admin.AdminPropertyResponse;
import com.toletboards.dto.admin.UserListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AdminService {

    AdminDashboardResponse getDashboard();

    Page<UserListResponse> getUsers(

        int page,

        int size,

        String search,

        String role

);


        AdminUserStatsResponse getUserStats();


        Page<AdminPropertyResponse> getProperties(

        int page,

        int size,

        String search,

        String status,

        String propertyType
);



PropertyResponse approveProperty(Long propertyId);

PropertyResponse rejectProperty(Long propertyId);

PropertyResponse markPending(Long propertyId);


void deleteProperty(Long propertyId);

PropertyResponse activateProperty(Long propertyId);

PropertyResponse deactivateProperty(Long propertyId);

Page<AdminVisitResponse> getVisits(
        int page,
        int size,
        String status);

}