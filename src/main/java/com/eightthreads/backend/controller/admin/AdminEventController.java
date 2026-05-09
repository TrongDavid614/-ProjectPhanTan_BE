package com.eightthreads.backend.controller.admin;

import com.eightthreads.backend.common.enums.EventStatus;
import com.eightthreads.backend.dto.request.admin.AdminEventCreateRequest;
import com.eightthreads.backend.dto.request.admin.AdminEventUpdateRequest;
import com.eightthreads.backend.dto.response.ApiResponse;
import com.eightthreads.backend.dto.response.admin.AdminEventResponse;
import com.eightthreads.backend.service.admin.AdminEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    @PostMapping
    public ResponseEntity<ApiResponse<AdminEventResponse>> createEvent(@Valid @RequestBody AdminEventCreateRequest request) {
        AdminEventResponse created = adminEventService.createEvent(request);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "Tạo sự kiện thành công", created), HttpStatus.CREATED);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponse<AdminEventResponse>> updateEvent(
            @PathVariable String eventId,
            @Valid @RequestBody AdminEventUpdateRequest request) {
        AdminEventResponse updated = adminEventService.updateEvent(eventId, request);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Cập nhật sự kiện thành công", updated));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable String eventId) {
        adminEventService.deleteEvent(eventId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Xóa sự kiện thành công", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminEventResponse>>> getAllEvents() {
        List<AdminEventResponse> events = adminEventService.getAllEvents();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách sự kiện thành công", events));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<AdminEventResponse>> getEventById(@PathVariable String eventId) {
        AdminEventResponse event = adminEventService.getEventById(eventId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Lấy sự kiện thành công", event));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AdminEventResponse>>> searchEvents(@RequestParam String keyword) {
        List<AdminEventResponse> events = adminEventService.searchEvents(keyword);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Tìm kiếm sự kiện thành công", events));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<List<AdminEventResponse>>> filterByStatus(@RequestParam String status) {
        EventStatus eventStatus = EventStatus.fromValue(status);
        List<AdminEventResponse> events = adminEventService.filterByStatus(eventStatus);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Lọc sự kiện theo trạng thái thành công", events));
    }
}
