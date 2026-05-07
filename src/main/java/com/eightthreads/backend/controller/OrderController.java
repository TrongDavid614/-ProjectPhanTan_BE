package com.eightthreads.backend.controller;

import com.eightthreads.backend.dto.request.OrderCreateRequest;
import com.eightthreads.backend.dto.response.OrderCreateResponse;
import com.eightthreads.backend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(
            @Valid @RequestBody OrderCreateRequest request,
            HttpServletRequest httpRequest) throws UnsupportedEncodingException {

        OrderCreateResponse response = orderService.createOrderAndGetPaymentUrl(request, httpRequest);
        return ResponseEntity.ok(response);
    }
}