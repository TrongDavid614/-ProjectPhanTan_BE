package com.eightthreads.backend.repository;

import com.eightthreads.backend.common.enums.OrderStatus;
import com.eightthreads.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser_UserIdOrderByCreatedAtDesc(String userId);
    List<Order> findByStatus(OrderStatus status);
    long countByEvent_CreatedBy_UserId(String userId);

    @Query("""
            SELECT COALESCE(SUM(o.totalAmount), 0)
            FROM Order o
            WHERE o.status = :status
            """)
    BigDecimal sumTotalAmountByStatus(@Param("status") OrderStatus status);

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.status = :status
          AND o.event.createdBy.userId = :userId
        """)
    BigDecimal sumTotalAmountByStatusAndEventCreatedBy_UserId(
        @Param("status") OrderStatus status,
        @Param("userId") String userId);
}