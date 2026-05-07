package com.eightthreads.backend.repository;

import com.eightthreads.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser_UserIdOrderByCreatedAtDesc(String userId);
}