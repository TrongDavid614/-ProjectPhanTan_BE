package com.eightthreads.backend.dto.response.admin;

import com.eightthreads.backend.entity.VoucherType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AdminVoucherResponse {
    private String voucherId;
    private String voucherName;
    private String conditions;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private String promotion;
    private VoucherType voucherType;
    private BigDecimal value;
    private List<String> eventIds;
}

