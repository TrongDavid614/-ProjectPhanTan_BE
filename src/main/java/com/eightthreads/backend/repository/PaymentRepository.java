package com.eightthreads.backend.repository;

import com.eightthreads.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
	Optional<Payment> findByOrder_OrderId(String orderId);
	boolean existsByOrder_OrderId(String orderId);
	boolean existsByTxnRef(String txnRef);
}