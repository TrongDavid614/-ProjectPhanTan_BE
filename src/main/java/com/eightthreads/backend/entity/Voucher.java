package com.eightthreads.backend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vouchers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {
    @Id
    @Column(name = "voucher_id", length = 50)
    private String voucherId;

    @Column(name = "voucher_name", nullable = false)
    private String voucherName;

    @Column(columnDefinition = "TEXT")
    private String conditions;

    @Column(name = "time_start")
    private LocalDateTime timeStart;

    @Column(name = "time_end")
    private LocalDateTime timeEnd;

    private String promotion;

    @Convert(converter = VoucherTypeConverter.class)
    @Column(name = "voucher_type", nullable = false ,length = 50)
    private VoucherType voucherType;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal value;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "Voucher_Events",
            joinColumns = @JoinColumn(name = "voucher_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @JsonIgnore
    private List<Event> events;
}
