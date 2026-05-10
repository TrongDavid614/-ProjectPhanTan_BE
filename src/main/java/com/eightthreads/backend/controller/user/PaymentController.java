package com.eightthreads.backend.controller.user;

import com.eightthreads.backend.common.enums.OrderStatus;
import com.eightthreads.backend.config.VNPAYConfig;
import com.eightthreads.backend.service.user.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"/api/user/payment", "/api/v1/payment"})
@RequiredArgsConstructor
public class PaymentController {

    private final OrderService orderService;
    private final VNPAYConfig vnpayConfig;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @GetMapping("/vnpay-return")
    public ResponseEntity<Void> vnpayReturn(HttpServletRequest request) {
        System.out.println("========== VNPAY RETURN ĐƯỢC GỌI ==========");

        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                fields.put(fieldName, fieldValue);
            }
        }

        String secureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        String signValue = VNPAYConfig.hashAllFields(fields, vnpayConfig.getSecretKey());

        System.out.println("Hash từ VNPAY: " + secureHash);
        System.out.println("Hash backend tạo: " + signValue);

        String redirectBase = buildFrontendApiReturnUrl(request);

        if (signValue.equals(secureHash)) {
            System.out.println("=> CHỮ KÝ HỢP LỆ");

            String orderId = request.getParameter("vnp_TxnRef");
            String responseCode = request.getParameter("vnp_ResponseCode");
            String transactionNo = request.getParameter("vnp_TransactionNo");

            System.out.println("Order ID: " + orderId);
            System.out.println("Response Code: " + responseCode);

            if ("00".equals(responseCode)) {
                System.out.println("=> THANH TOÁN THÀNH CÔNG");
                orderService.processSuccessfulPayment(orderId, transactionNo);
                String redirectUrl = redirectBase
                        + "?status=success"
                        + "&orderId=" + urlEncode(orderId)
                        + "&transactionNo=" + urlEncode(transactionNo);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(redirectUrl))
                        .build();
            } else {
                System.out.println("=> THANH TOÁN THẤT BẠI");
                orderService.updateOrderStatus(orderId, OrderStatus.FAILED);
                String redirectUrl = redirectBase
                        + "?status=failed"
                        + "&orderId=" + urlEncode(orderId)
                        + "&message=" + urlEncode(responseCode);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(redirectUrl))
                        .build();
            }
        }

        System.out.println("=> CHỮ KÝ KHÔNG HỢP LỆ");
        String redirectUrl = redirectBase
                + "?status=error&message=invalid-signature";
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    private String buildFrontendApiReturnUrl(HttpServletRequest request) {
        if (StringUtils.hasText(frontendUrl)) {
            String base = frontendUrl.endsWith("/") ? frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl;
            return base + "/api/payment/vnpay-return";
        }

        String scheme = getHeader(request, "x-forwarded-proto");
        if (!StringUtils.hasText(scheme)) {
            scheme = request.getScheme();
        }

        String host = getHeader(request, "x-forwarded-host");
        if (!StringUtils.hasText(host)) {
            host = request.getHeader("Host");
        }

        if (!StringUtils.hasText(host)) {
            throw new IllegalStateException("Unable to resolve frontend host for VNPAY redirect");
        }

        return scheme + "://" + host + "/api/payment/vnpay-return";
    }

    private String getHeader(HttpServletRequest request, String headerName) {
        String value = request.getHeader(headerName);
        return StringUtils.hasText(value) ? value : request.getHeader(headerName.toUpperCase());
    }

    private String urlEncode(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
    }
}