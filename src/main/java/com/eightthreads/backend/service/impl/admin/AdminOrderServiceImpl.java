package com.eightthreads.backend.service.impl.admin;

import com.eightthreads.backend.common.enums.OrderStatus;
import com.eightthreads.backend.dto.request.admin.AdminOrderStatusUpdateRequest;
import com.eightthreads.backend.dto.response.admin.AdminOrderDetailResponse;
import com.eightthreads.backend.dto.response.admin.AdminOrderItemResponse;
import com.eightthreads.backend.dto.response.admin.AdminOrderSummaryResponse;
import com.eightthreads.backend.entity.Order;
import com.eightthreads.backend.entity.OrderItem;
import com.eightthreads.backend.entity.Payment;
import com.eightthreads.backend.repository.OrderRepository;
import com.eightthreads.backend.service.admin.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<AdminOrderSummaryResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdminOrderDetailResponse getOrderDetail(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        return toDetailResponse(order);
    }

    @Override
    public List<AdminOrderSummaryResponse> filterByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdminOrderDetailResponse updateOrderStatus(String orderId, AdminOrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setStatus(request.getStatus());
        return toDetailResponse(orderRepository.save(order));
    }


    private AdminOrderSummaryResponse toSummaryResponse(Order order) {
        return AdminOrderSummaryResponse.builder()
                .orderId(order.getOrderId())
                .eventId(order.getEvent() != null ? order.getEvent().getEventId() : null)
                .eventName(order.getEvent() != null ? order.getEvent().getName() : null)
                .userId(order.getUser() != null ? order.getUser().getUserId() : null)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private AdminOrderDetailResponse toDetailResponse(Order order) {
        Payment payment = order.getPayment();
        List<AdminOrderItemResponse> items = order.getOrderItems() == null ? List.of() :
                order.getOrderItems().stream().map(this::toItemResponse).collect(Collectors.toList());

        return AdminOrderDetailResponse.builder()
                .orderId(order.getOrderId())
                .eventId(order.getEvent() != null ? order.getEvent().getEventId() : null)
                .eventName(order.getEvent() != null ? order.getEvent().getName() : null)
                .userId(order.getUser() != null ? order.getUser().getUserId() : null)
                .userEmail(order.getUser() != null ? order.getUser().getEmail() : null)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .paymentMethod(payment != null ? payment.getMethod() : null)
                .paymentStatus(payment != null ? payment.getStatus() : null)
                .items(items)
                .build();
    }

    private AdminOrderItemResponse toItemResponse(OrderItem item) {
        return AdminOrderItemResponse.builder()
                .ticketTypeId(item.getTicketType() != null ? item.getTicketType().getTicketTypeId() : null)
                .ticketTypeName(item.getTicketType() != null ? item.getTicketType().getName() : null)
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}
