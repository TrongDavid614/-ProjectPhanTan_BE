package com.eightthreads.backend.dto.response.admin;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AdminDashboardResponse {
    private long totalEvents;
    private long totalOrders;
    private BigDecimal totalRevenue;
    private long totalTicketsSold;
}

