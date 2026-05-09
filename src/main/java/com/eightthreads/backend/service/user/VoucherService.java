package com.eightthreads.backend.service.user;

import com.eightthreads.backend.entity.Voucher;

import java.util.List;

public interface VoucherService {
    List<Voucher> getAllActiveVouchers();
}

