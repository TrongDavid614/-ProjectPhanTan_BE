package com.eightthreads.backend.controller.user;

import com.eightthreads.backend.dto.request.user.OrderCreateRequest;
import com.eightthreads.backend.dto.response.user.OrderCreateResponse;
import com.eightthreads.backend.dto.response.user.OrderHistoryResponse;
import com.eightthreads.backend.service.user.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class UserOrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(
            @Valid @RequestBody OrderCreateRequest request,
            HttpServletRequest httpRequest) throws UnsupportedEncodingException {

        OrderCreateResponse response = orderService.createOrderAndGetPaymentUrl(request, httpRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<OrderHistoryResponse>> getOrderHistory(@PathVariable("userId") String userId) {
        List<OrderHistoryResponse> history = orderService.getUserOrderHistory(userId);
        return ResponseEntity.ok(history);
    }
}
