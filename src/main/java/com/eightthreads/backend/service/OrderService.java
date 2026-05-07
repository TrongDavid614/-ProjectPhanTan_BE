package com.eightthreads.backend.service;

import com.eightthreads.backend.dto.request.OrderCreateRequest;
import com.eightthreads.backend.dto.response.OrderCreateResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface OrderService {

    /**
     * Giai đoạn 1: Khi người dùng bấm "Thanh toán"
     * Lưu giỏ hàng vào Database (trạng thái 'pending') và sinh ra đường link VNPay.
     *
     * @param request Thông tin giỏ hàng từ Frontend gửi lên (User, Event, danh sách Ticket...)
     * @param httpRequest Request hiện tại để lấy địa chỉ IP người dùng (VNPay yêu cầu)
     * @return OrderCreateResponse chứa orderId, paymentUrl, totalAmount
     */
    OrderCreateResponse createOrderAndGetPaymentUrl(OrderCreateRequest request, HttpServletRequest httpRequest) throws UnsupportedEncodingException;

    /**
     * Giai đoạn 2: Khi VNPay trả kết quả về (IPN / Return URL)
     * Cập nhật trạng thái đơn hàng dựa trên kết quả giao dịch.
     *
     * @param orderId Mã đơn hàng (vnp_TxnRef)
     * @param status Trạng thái mới (ví dụ: "paid", "failed")
     */
    void updateOrderStatus(String orderId, String status);
    void processSuccessfulPayment(String orderId, String vnpayTransactionNo);
}