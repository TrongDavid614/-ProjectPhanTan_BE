package com.eightthreads.backend.controller;

import com.eightthreads.backend.dto.response.ApiResponse;
import com.eightthreads.backend.entity.Voucher;
import com.eightthreads.backend.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Voucher>>> getVouchers() {
        return ResponseEntity.ok(new ApiResponse<>(
                200,
                "Lấy danh sách voucher thành công",
                voucherService.getAllActiveVouchers()
        ));
    }
}
