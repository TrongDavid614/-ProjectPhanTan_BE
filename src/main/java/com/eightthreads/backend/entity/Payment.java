package com.eightthreads.backend.entity;

import com.eightthreads.backend.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "Payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @Column(name = "payment_id", length = 50)
    private String paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(length = 20)
    private String method = "vnpay";

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(length = 10)
    private String currency = "VND";

    @Column(length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "txn_ref", length = 100)
    private String txnRef;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;
}