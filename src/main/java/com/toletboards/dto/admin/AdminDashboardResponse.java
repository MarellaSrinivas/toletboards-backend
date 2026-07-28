package com.toletboards.dto.admin;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardResponse {

    private Long totalUsers;

    private Long totalProperties;

    private Long approvedProperties;

    private Long pendingProperties;

    private Long rejectedProperties;

    private Long todayVisits;

    private List<RecentPropertyResponse> recentProperties;

    private List<RecentVisitResponse> recentVisits;

}