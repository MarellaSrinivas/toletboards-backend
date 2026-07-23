package com.toletboards.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private long totalListings;

    private long activeProperties;

    private long pendingApproval;

    private long approvedProperties;

    private long totalVisits;
}