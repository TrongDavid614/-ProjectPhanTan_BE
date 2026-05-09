package com.eightthreads.backend.service.impl.user;

import com.eightthreads.backend.common.enums.OrderStatus;
import com.eightthreads.backend.common.enums.PaymentStatus;
import com.eightthreads.backend.dto.request.user.OrderCreateRequest;
import com.eightthreads.backend.dto.request.user.OrderItemRequest;
import com.eightthreads.backend.dto.response.user.OrderCreateResponse;
import com.eightthreads.backend.dto.response.user.OrderHistoryResponse;
import com.eightthreads.backend.entity.*;
import com.eightthreads.backend.repository.*;
import com.eightthreads.backend.service.user.OrderService;
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
import java.util.stream.Collectors;

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
        // Try to find user by primary id first; if not found, fall back to email lookup.
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            user = userRepository.findByEmail(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay nguoi dung: " + request.getUserId()));
        }
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay su kien: " + request.getEventId()));

        String newOrderId = "ORD_" + System.currentTimeMillis();
        Order order = Order.builder()
                .orderId(newOrderId)
                .user(user)
                .event(event)
                .status(OrderStatus.PENDING)
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
    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setStatus(status);

        orderRepository.save(order);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processSuccessfulPayment(String orderId, String vnpayTransactionNo) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng: " + orderId));

        if (paymentRepository.existsByOrder_OrderId(orderId) || OrderStatus.PAID.equals(order.getStatus())) {
            return;
        }

        if (vnpayTransactionNo != null && !vnpayTransactionNo.isBlank() && paymentRepository.existsByTxnRef(vnpayTransactionNo)) {
            return;
        }

        order.setStatus(OrderStatus.PAID);

        Payment payment = Payment.builder()
                .paymentId("PAY_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .order(order)
                .method("VNPAY")
                .txnRef(vnpayTransactionNo)
                .amount(order.getTotalAmount())
                .status(PaymentStatus.SUCCESS)
                .paidAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);
        order.setPayment(payment);

        List<Ticket> generatedTickets = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

            TicketType ticketType = item.getTicketType();
            int currentSold = ticketType.getSoldQuantity() == null ? 0 : ticketType.getSoldQuantity();
            ticketType.setSoldQuantity(currentSold + item.getQuantity());
            ticketTypeRepository.save(ticketType);

            for (int i = 0; i < item.getQuantity(); i++) {
                String uniqueCode = "TKT-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

                Ticket ticket = Ticket.builder()
                        .ticketId(UUID.randomUUID().toString())
                        .event(order.getEvent())
                        .ticketType(ticketType)
                        .order(order)
                        .owner(order.getUser())
                        .qrCode(uniqueCode)
                        .status("valid")
                        .build();
                generatedTickets.add(ticket);
            }
        }
        ticketRepository.saveAll(generatedTickets);
        orderRepository.save(order);
    }
    @Override
    public List<OrderHistoryResponse> getUserOrderHistory(String userId) {
        List<Order> orders = orderRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);

        return orders.stream().map(order -> {
            String payMethod = (order.getPayment() != null) ? order.getPayment().getMethod() : "Chưa thanh toán";

            return OrderHistoryResponse.builder()
                    .orderId(order.getOrderId())
                    .totalAmount(order.getTotalAmount())
                    .status(order.getStatus())
                    .createdAt(order.getCreatedAt())
                    .eventName(order.getEvent().getName())
                    .eventImg(order.getEvent().getImg())
                    .paymentMethod(payMethod)
                    .build();
        }).collect(Collectors.toList());
    }
}
