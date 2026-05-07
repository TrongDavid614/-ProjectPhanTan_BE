package com.eightthreads.backend.repository;

import com.eightthreads.backend.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String> {

    @Query("""
            SELECT v
            FROM Voucher v
            WHERE (v.timeStart IS NULL OR v.timeStart <= :now)
              AND (v.timeEnd IS NULL OR v.timeEnd >= :now)
            ORDER BY v.createdAt DESC
            """)
    List<Voucher> findAvailableVouchers(@Param("now") LocalDateTime now);
}
