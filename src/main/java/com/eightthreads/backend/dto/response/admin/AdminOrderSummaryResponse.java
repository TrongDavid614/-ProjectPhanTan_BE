package com.eightthreads.backend.dto.response.admin;

import com.eightthreads.backend.common.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminOrderSummaryResponse {
    private String orderId;
    private String eventId;
    private String eventName;
    private String userId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
}

