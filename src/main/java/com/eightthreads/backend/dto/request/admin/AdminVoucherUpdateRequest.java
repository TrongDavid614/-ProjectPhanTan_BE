package com.eightthreads.backend.dto.request.admin;

import com.eightthreads.backend.entity.VoucherType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminVoucherUpdateRequest {

    @NotBlank(message = "Tên voucher không được để trống")
    private String voucherName;

    private String conditions;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private String promotion;

    @NotNull(message = "Loại voucher không được để trống")
    private VoucherType voucherType;

    @NotNull(message = "Giá trị voucher không được để trống")
    private BigDecimal value;
}

