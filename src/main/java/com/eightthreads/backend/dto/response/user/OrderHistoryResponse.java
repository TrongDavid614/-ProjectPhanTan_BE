package com.eightthreads.backend.dto.response.user;

import com.eightthreads.backend.common.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderHistoryResponse {
    private String orderId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;

    private String eventName;
    private String eventImg;

    private String paymentMethod;
}
