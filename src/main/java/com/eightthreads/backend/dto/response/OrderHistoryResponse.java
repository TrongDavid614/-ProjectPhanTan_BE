package com.eightthreads.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderHistoryResponse {
    private String orderId;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;

    private String eventName;
    private String eventImg;

    private String paymentMethod;
}