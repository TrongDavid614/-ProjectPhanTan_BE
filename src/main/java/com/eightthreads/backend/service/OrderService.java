package com.eightthreads.backend.service;

import com.eightthreads.backend.dto.request.OrderCreateRequest;
import com.eightthreads.backend.dto.response.OrderCreateResponse;
import com.eightthreads.backend.dto.response.OrderHistoryResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface OrderService {

    OrderCreateResponse createOrderAndGetPaymentUrl(OrderCreateRequest request, HttpServletRequest httpRequest) throws UnsupportedEncodingException;
    void updateOrderStatus(String orderId, String status);
    List<OrderHistoryResponse> getUserOrderHistory(String email);
    void processSuccessfulPayment(String orderId, String vnpayTransactionNo);
}