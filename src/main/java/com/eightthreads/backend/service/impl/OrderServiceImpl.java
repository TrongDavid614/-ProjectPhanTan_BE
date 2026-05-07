package com.eightthreads.backend.service.impl;

import com.eightthreads.backend.dto.request.OrderCreateRequest;
import com.eightthreads.backend.dto.request.OrderItemRequest;
import com.eightthreads.backend.dto.response.OrderCreateResponse;
import com.eightthreads.backend.entity.*;
import com.eightthreads.backend.repository.*;
import com.eightthreads.backend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final PaymentServiceImpl paymentService;
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public OrderCreateResponse createOrderAndGetPaymentUrl(OrderCreateRequest request, HttpServletRequest httpRequest) throws UnsupportedEncodingException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay nguoi dung: " + request.getUserId()));
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay su kien: " + request.getEventId()));

        String newOrderId = "ORD_" + System.currentTimeMillis();
        Order order = Order.builder()
                .orderId(newOrderId)
                .user(user)
                .event(event)
                .status("pending")
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemReq : request.getItems()) {
            TicketType ticketType = ticketTypeRepository.findById(itemReq.getTicketTypeId())
                    .orElseThrow(() -> new RuntimeException("Khong tim thay loai ve: " + itemReq.getTicketTypeId()));

            BigDecimal unitPrice = ticketType.getPrice();
            BigDecimal subtotal = unitPrice.multiply(new BigDecimal(itemReq.getQuantity()));

            int currentSold = ticketType.getSoldQuantity() == null ? 0 : ticketType.getSoldQuantity();
            int totalQuantity = ticketType.getTotalQuantity() == null ? 0 : ticketType.getTotalQuantity();
            if (currentSold + itemReq.getQuantity() > totalQuantity) {
                throw new RuntimeException("Số lượng vé không đủ cho hạng vé: " + ticketType.getName());
            }

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .ticketType(ticketType)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            orderItems.add(item);
            totalAmount = totalAmount.add(subtotal);
        }


        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);

        String paymentUrl = paymentService.createPaymentUrl(
                order.getTotalAmount().longValue(),
                "Thanh toan don hang " + newOrderId,
                newOrderId,
                httpRequest
        );
        
        return OrderCreateResponse.builder()
                .orderId(newOrderId)
                .paymentUrl(paymentUrl)
                .totalAmount(order.getTotalAmount().longValue())
                .build();
    }
    @Override
    @Transactional
    public void updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setStatus(status);

        orderRepository.save(order);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processSuccessfulPayment(String orderId, String vnpayTransactionNo) {
        // 1. Lấy đơn hàng PENDING ra
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng: " + orderId));

        // Kiểm tra chống xử lý đúp (Idempotency)
        if (paymentRepository.existsByOrder_OrderId(orderId) || "paid".equals(order.getStatus())) {
            return;
        }

        if (vnpayTransactionNo != null && !vnpayTransactionNo.isBlank() && paymentRepository.existsByTxnRef(vnpayTransactionNo)) {
            return;
        }

        // 2. Cập nhật trạng thái
        order.setStatus("paid");

        // 3. Tạo lịch sử giao dịch (Payment)
        Payment payment = Payment.builder()
                // Dùng UUID 8 ký tự để đảm bảo không bao giờ trùng lặp ID
                .paymentId("PAY_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .order(order)
                .method("VNPAY")
                .txnRef(vnpayTransactionNo)
                .amount(order.getTotalAmount())
                .status("success")
                .paidAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);
        order.setPayment(payment);

        // 4. Cập nhật sold_quantity và Sinh mã vé (Gộp chung 1 vòng lặp để tối ưu hiệu năng)
        List<Ticket> generatedTickets = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

            // --- Xử lý Ticket Type (Cộng số lượng) ---
            TicketType ticketType = item.getTicketType();
            int currentSold = ticketType.getSoldQuantity() == null ? 0 : ticketType.getSoldQuantity();
            ticketType.setSoldQuantity(currentSold + item.getQuantity());
            // (Nhờ @Transactional, JPA sẽ tự động update xuống DB, nhưng gọi save() vẫn an toàn)
            ticketTypeRepository.save(ticketType);

            // --- Xử lý Sinh Vé tương ứng với số lượng ---
            for (int i = 0; i < item.getQuantity(); i++) {
                String uniqueCode = "TKT-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

                Ticket ticket = Ticket.builder()
                        .ticketId(UUID.randomUUID().toString())
                        .event(order.getEvent())
                        .ticketType(ticketType) // Dùng luôn biến ticketType ở trên
                        .order(order)
                        .owner(order.getUser())
                        .qrCode(uniqueCode)
                        .status("valid")
                        .build();
                generatedTickets.add(ticket);
            }
        }

        // 5. Lưu toàn bộ vé vào DB trong 1 lần (Batch Insert)
        ticketRepository.saveAll(generatedTickets);

        // Lưu Order để chốt trạng thái
        orderRepository.save(order);

        // 6. Gửi Email thông báo chứa mã vé cho khách hàng (Nếu có)
        // emailService.sendTicketEmail(order.getUser(), generatedTickets);
    }
}
