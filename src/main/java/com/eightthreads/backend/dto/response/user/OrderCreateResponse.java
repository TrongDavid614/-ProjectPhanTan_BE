package com.eightthreads.backend.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateResponse {
    private String orderId;
    private String paymentUrl;
    private Long totalAmount;
}

