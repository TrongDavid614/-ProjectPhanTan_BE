package com.eightthreads.backend.controller.admin;

import com.eightthreads.backend.common.enums.OrderStatus;
import com.eightthreads.backend.dto.request.admin.AdminOrderStatusUpdateRequest;
import com.eightthreads.backend.dto.response.ApiResponse;
import com.eightthreads.backend.dto.response.admin.AdminOrderDetailResponse;
import com.eightthreads.backend.dto.response.admin.AdminOrderSummaryResponse;
import com.eightthreads.backend.service.admin.AdminOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminOrderSummaryResponse>>> getAllOrders() {
        List<AdminOrderSummaryResponse> orders = adminOrderService.getAllOrders();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách đơn hàng thành công", orders));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<AdminOrderDetailResponse>> getOrderDetail(@PathVariable String orderId) {
        AdminOrderDetailResponse order = adminOrderService.getOrderDetail(orderId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Lấy chi tiết đơn hàng thành công", order));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<List<AdminOrderSummaryResponse>>> filterByStatus(@RequestParam String status) {
        OrderStatus orderStatus = OrderStatus.fromValue(status);
        List<AdminOrderSummaryResponse> orders = adminOrderService.filterByStatus(orderStatus);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Lọc đơn hàng theo trạng thái thành công", orders));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<AdminOrderDetailResponse>> updateOrderStatus(
            @PathVariable String orderId,
            @Valid @RequestBody AdminOrderStatusUpdateRequest request) {
        AdminOrderDetailResponse updated = adminOrderService.updateOrderStatus(orderId, request);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Cập nhật trạng thái đơn hàng thành công", updated));
    }
}
