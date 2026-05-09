package com.eightthreads.backend.dto.response.admin;

import com.eightthreads.backend.common.enums.OrderStatus;
import com.eightthreads.backend.common.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AdminOrderDetailResponse {
    private String orderId;
    private String eventId;
    private String eventName;
    private String userId;
    private String userEmail;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private List<AdminOrderItemResponse> items;
}

