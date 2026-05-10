package com.eightthreads.backend.controller.user;

import com.eightthreads.backend.dto.request.user.OrderCreateRequest;
import com.eightthreads.backend.dto.response.user.OrderCreateResponse;
import com.eightthreads.backend.dto.response.user.OrderHistoryResponse;
import com.eightthreads.backend.entity.User;
import com.eightthreads.backend.service.user.OrderService;
import com.eightthreads.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/api/user/orders", "/api/v1/orders"})
@RequiredArgsConstructor
public class UserOrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(
            @Valid @RequestBody OrderCreateRequest request,
            HttpServletRequest httpRequest) throws UnsupportedEncodingException {

        OrderCreateResponse response = orderService.createOrderAndGetPaymentUrl(request, httpRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/history/{identifier}")
    public ResponseEntity<List<OrderHistoryResponse>> getOrderHistory(@PathVariable("identifier") String identifier) {
        String userId = resolveUserId(identifier);
        List<OrderHistoryResponse> history = orderService.getUserOrderHistory(userId);
        return ResponseEntity.ok(history);
    }

    private String resolveUserId(String identifier) {
        Optional<User> byId = userRepository.findById(identifier);
        if (byId.isPresent()) {
            return byId.get().getUserId();
        }

        return userRepository.findByEmail(identifier)
                .map(User::getUserId)
                .orElse(identifier);
    }
}
