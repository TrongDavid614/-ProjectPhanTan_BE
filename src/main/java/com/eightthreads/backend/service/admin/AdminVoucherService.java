package com.eightthreads.backend.service.admin;

import com.eightthreads.backend.dto.request.admin.AdminVoucherAssignEventRequest;
import com.eightthreads.backend.dto.request.admin.AdminVoucherCreateRequest;
import com.eightthreads.backend.dto.request.admin.AdminVoucherUpdateRequest;
import com.eightthreads.backend.dto.response.admin.AdminVoucherResponse;

public interface AdminVoucherService {
    AdminVoucherResponse createVoucher(AdminVoucherCreateRequest request);
    AdminVoucherResponse updateVoucher(String voucherId, AdminVoucherUpdateRequest request);
    void deleteVoucher(String voucherId);
    AdminVoucherResponse assignVoucherToEvent(String voucherId, AdminVoucherAssignEventRequest request);
}

