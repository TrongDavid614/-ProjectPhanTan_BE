package com.eightthreads.backend.service.impl.admin;

import com.eightthreads.backend.dto.request.admin.AdminVoucherAssignEventRequest;
import com.eightthreads.backend.dto.request.admin.AdminVoucherCreateRequest;
import com.eightthreads.backend.dto.request.admin.AdminVoucherUpdateRequest;
import com.eightthreads.backend.dto.response.admin.AdminVoucherResponse;
import com.eightthreads.backend.entity.Event;
import com.eightthreads.backend.entity.Voucher;
import com.eightthreads.backend.repository.EventRepository;
import com.eightthreads.backend.repository.VoucherRepository;
import com.eightthreads.backend.service.admin.AdminVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminVoucherServiceImpl implements AdminVoucherService {

    private final VoucherRepository voucherRepository;
    private final EventRepository eventRepository;

    @Override
    public AdminVoucherResponse createVoucher(AdminVoucherCreateRequest request) {
        Voucher voucher = Voucher.builder()
                .voucherId("VOU_" + System.currentTimeMillis())
                .voucherName(request.getVoucherName())
                .conditions(request.getConditions())
                .timeStart(request.getTimeStart())
                .timeEnd(request.getTimeEnd())
                .promotion(request.getPromotion())
                .voucherType(request.getVoucherType())
                .value(request.getValue())
                .build();

        Voucher saved = voucherRepository.save(voucher);
        return toResponse(saved);
    }

    @Override
    public AdminVoucherResponse updateVoucher(String voucherId, AdminVoucherUpdateRequest request) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher"));

        voucher.setVoucherName(request.getVoucherName());
        voucher.setConditions(request.getConditions());
        voucher.setTimeStart(request.getTimeStart());
        voucher.setTimeEnd(request.getTimeEnd());
        voucher.setPromotion(request.getPromotion());
        voucher.setVoucherType(request.getVoucherType());
        voucher.setValue(request.getValue());

        return toResponse(voucherRepository.save(voucher));
    }

    @Override
    public void deleteVoucher(String voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher"));
        voucherRepository.delete(voucher);
    }

    @Override
    public AdminVoucherResponse assignVoucherToEvent(String voucherId, AdminVoucherAssignEventRequest request) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher"));
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));

        List<Event> events = voucher.getEvents();
        if (events == null) {
            events = new ArrayList<>();
        }
        if (!events.contains(event)) {
            events.add(event);
        }
        voucher.setEvents(events);

        return toResponse(voucherRepository.save(voucher));
    }

    private AdminVoucherResponse toResponse(Voucher voucher) {
        List<String> eventIds = voucher.getEvents() == null ? List.of() :
                voucher.getEvents().stream().map(Event::getEventId).collect(Collectors.toList());

        return AdminVoucherResponse.builder()
                .voucherId(voucher.getVoucherId())
                .voucherName(voucher.getVoucherName())
                .conditions(voucher.getConditions())
                .timeStart(voucher.getTimeStart())
                .timeEnd(voucher.getTimeEnd())
                .promotion(voucher.getPromotion())
                .voucherType(voucher.getVoucherType())
                .value(voucher.getValue())
                .eventIds(eventIds)
                .build();
    }
}

