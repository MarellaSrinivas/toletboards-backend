package com.toletboards.dto.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserStatsResponse {

    private long totalUsers;

    private long totalOwners;

    private long totalAgents;

    private long totalAdmins;

    private long verifiedUsers;

    

}