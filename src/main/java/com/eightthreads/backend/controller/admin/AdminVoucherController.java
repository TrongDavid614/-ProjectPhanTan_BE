package com.eightthreads.backend.controller.admin;

import com.eightthreads.backend.dto.request.admin.AdminVoucherAssignEventRequest;
import com.eightthreads.backend.dto.request.admin.AdminVoucherCreateRequest;
import com.eightthreads.backend.dto.request.admin.AdminVoucherUpdateRequest;
import com.eightthreads.backend.dto.response.ApiResponse;
import com.eightthreads.backend.dto.response.admin.AdminVoucherResponse;
import com.eightthreads.backend.service.admin.AdminVoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/vouchers")
@RequiredArgsConstructor
public class AdminVoucherController {

    private final AdminVoucherService adminVoucherService;

    @PostMapping
    public ResponseEntity<ApiResponse<AdminVoucherResponse>> createVoucher(@Valid @RequestBody AdminVoucherCreateRequest request) {
        AdminVoucherResponse created = adminVoucherService.createVoucher(request);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "Tạo voucher thành công", created), HttpStatus.CREATED);
    }

    @PutMapping("/{voucherId}")
    public ResponseEntity<ApiResponse<AdminVoucherResponse>> updateVoucher(
            @PathVariable String voucherId,
            @Valid @RequestBody AdminVoucherUpdateRequest request) {
        AdminVoucherResponse updated = adminVoucherService.updateVoucher(voucherId, request);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Cập nhật voucher thành công", updated));
    }

    @DeleteMapping("/{voucherId}")
    public ResponseEntity<ApiResponse<Void>> deleteVoucher(@PathVariable String voucherId) {
        adminVoucherService.deleteVoucher(voucherId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Xóa voucher thành công", null));
    }

    @PostMapping("/{voucherId}/assign-event")
    public ResponseEntity<ApiResponse<AdminVoucherResponse>> assignVoucherToEvent(
            @PathVariable String voucherId,
            @Valid @RequestBody AdminVoucherAssignEventRequest request) {
        AdminVoucherResponse updated = adminVoucherService.assignVoucherToEvent(voucherId, request);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Gán voucher cho sự kiện thành công", updated));
    }
}
