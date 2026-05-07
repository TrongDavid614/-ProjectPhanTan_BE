package com.eightthreads.backend.controller;

import com.eightthreads.backend.config.VNPAYConfig;
import com.eightthreads.backend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final OrderService orderService;
    private final VNPAYConfig vnpayConfig;

    @Value("${app.frontend.payment-return-url:http://localhost:3000/page/payment/return}")
    private String frontendPaymentReturnUrl;

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> vnpayReturn(HttpServletRequest request) {
        System.out.println("========== VNPAY RETURN ĐƯỢC GỌI ==========");

        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && !fieldValue.isEmpty()) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        // Kiểm tra chữ ký bảo mật
        String signValue = VNPAYConfig.hashAllFields(fields, vnpayConfig.getSecretKey());

        System.out.println("Mã Hash VNPAY gửi lên: " + vnp_SecureHash);
        System.out.println("Mã Hash Backend tính ra: " + signValue);

        if (signValue.equals(vnp_SecureHash)) {
            System.out.println("=> CHỮ KÝ HỢP LỆ!");
            String orderId = request.getParameter("vnp_TxnRef");
            String responseCode = request.getParameter("vnp_ResponseCode");
            String transactionNo = request.getParameter("vnp_TransactionNo");

            System.out.println("=> Mã Order: " + orderId + " | ResponseCode: " + responseCode);

            if ("00".equals(responseCode)) {
                System.out.println("=> GIAO DỊCH THÀNH CÔNG. ĐANG LƯU VÀO DATABASE...");
                orderService.processSuccessfulPayment(orderId, transactionNo);
                System.out.println("=> LƯU DATABASE XONG!");
                String redirectUrl = frontendPaymentReturnUrl
                        + "?status=success"
                        + "&orderId=" + orderId
                        + "&transactionNo=" + (transactionNo == null ? "" : transactionNo);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(redirectUrl))
                        .build();
            } else {
                System.out.println("=> GIAO DỊCH THẤT BẠI. CODE: " + responseCode);
                orderService.updateOrderStatus(orderId, "failed");
                String redirectUrl = frontendPaymentReturnUrl
                        + "?status=failed"
                        + "&orderId=" + orderId
                        + "&message=" + responseCode;
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(redirectUrl))
                        .build();
            }
        } else {
            System.out.println("=> LỖI: CHỮ KÝ KHÔNG KHỚP. TỪ CHỐI XỬ LÝ!");
            String redirectUrl = frontendPaymentReturnUrl + "?status=error&message=invalid-signature";
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(redirectUrl))
                    .build();
        }
    }
}