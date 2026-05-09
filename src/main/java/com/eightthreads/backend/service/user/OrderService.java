package com.eightthreads.backend.service.user;

import com.eightthreads.backend.common.enums.OrderStatus;
import com.eightthreads.backend.dto.request.user.OrderCreateRequest;
import com.eightthreads.backend.dto.response.user.OrderCreateResponse;
import com.eightthreads.backend.dto.response.user.OrderHistoryResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface OrderService {

    OrderCreateResponse createOrderAndGetPaymentUrl(OrderCreateRequest request, HttpServletRequest httpRequest) throws UnsupportedEncodingException;
    void updateOrderStatus(String orderId, OrderStatus status);
    List<OrderHistoryResponse> getUserOrderHistory(String email);
    void processSuccessfulPayment(String orderId, String vnpayTransactionNo);
}
