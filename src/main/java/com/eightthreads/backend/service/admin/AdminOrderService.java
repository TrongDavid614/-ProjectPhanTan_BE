package com.eightthreads.backend.service.admin;

import com.eightthreads.backend.common.enums.OrderStatus;
import com.eightthreads.backend.dto.request.admin.AdminOrderStatusUpdateRequest;
import com.eightthreads.backend.dto.response.admin.AdminOrderDetailResponse;
import com.eightthreads.backend.dto.response.admin.AdminOrderSummaryResponse;

import java.util.List;

public interface AdminOrderService {
    List<AdminOrderSummaryResponse> getAllOrders();
    AdminOrderDetailResponse getOrderDetail(String orderId);
    List<AdminOrderSummaryResponse> filterByStatus(OrderStatus status);
    AdminOrderDetailResponse updateOrderStatus(String orderId, AdminOrderStatusUpdateRequest request);
}

