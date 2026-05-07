package com.eightthreads.backend.service.impl;

import com.eightthreads.backend.entity.Voucher;
import com.eightthreads.backend.repository.VoucherRepository;
import com.eightthreads.backend.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;

    @Override
    public List<Voucher> getAllActiveVouchers() {
        return voucherRepository.findAvailableVouchers(LocalDateTime.now());
    }
}