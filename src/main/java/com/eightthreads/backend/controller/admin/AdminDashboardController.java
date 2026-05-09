package com.eightthreads.backend.controller.admin;

import com.eightthreads.backend.dto.response.ApiResponse;
import com.eightthreads.backend.dto.response.admin.AdminDashboardResponse;
import com.eightthreads.backend.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboardStats() {
        AdminDashboardResponse stats = adminDashboardService.getDashboardStats();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Lấy thống kê dashboard thành công", stats));
    }
}
