package com.eightthreads.backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    @NotBlank(message = "ID Người dùng không được để trống")
    private String userId;

    @NotBlank(message = "ID Sự kiện không được để trống")
    private String eventId;

    @NotEmpty(message = "Giỏ hàng không được để trống")
    @Valid
    private List<OrderItemRequest> items;

    private String voucherId;

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    private String paymentMethod;
}