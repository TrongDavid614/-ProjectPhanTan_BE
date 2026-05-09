package com.eightthreads.backend.controller.admin;

import com.eightthreads.backend.dto.request.admin.AdminTicketTypeCreateRequest;
import com.eightthreads.backend.dto.request.admin.AdminTicketTypeUpdateRequest;
import com.eightthreads.backend.dto.response.ApiResponse;
import com.eightthreads.backend.dto.response.admin.AdminTicketTypeResponse;
import com.eightthreads.backend.service.admin.AdminTicketTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tickets")
@RequiredArgsConstructor
public class AdminTicketController {

    private final AdminTicketTypeService adminTicketTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse<AdminTicketTypeResponse>> createTicketType(@Valid @RequestBody AdminTicketTypeCreateRequest request) {
        AdminTicketTypeResponse created = adminTicketTypeService.createTicketType(request);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "Tạo loại vé thành công", created), HttpStatus.CREATED);
    }

    @PutMapping("/{ticketTypeId}")
    public ResponseEntity<ApiResponse<AdminTicketTypeResponse>> updateTicketType(
            @PathVariable Long ticketTypeId,
            @Valid @RequestBody AdminTicketTypeUpdateRequest request) {
        AdminTicketTypeResponse updated = adminTicketTypeService.updateTicketType(ticketTypeId, request);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Cập nhật loại vé thành công", updated));
    }

    @DeleteMapping("/{ticketTypeId}")
    public ResponseEntity<ApiResponse<Void>> deleteTicketType(@PathVariable Long ticketTypeId) {
        adminTicketTypeService.deleteTicketType(ticketTypeId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Xóa loại vé thành công", null));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ApiResponse<List<AdminTicketTypeResponse>>> getTicketTypesByEvent(@PathVariable String eventId) {
        List<AdminTicketTypeResponse> ticketTypes = adminTicketTypeService.getTicketTypesByEvent(eventId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách loại vé thành công", ticketTypes));
    }
}
