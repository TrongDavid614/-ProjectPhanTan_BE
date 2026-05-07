package com.eightthreads.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateRequest {

    @NotBlank(message = "Tên sự kiện không được để trống")
    private String name;

    private String categoryId;
    private String description;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private LocalDateTime startTime;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    private LocalDateTime endTime;

    @NotNull(message = "Thời gian mở bán vé không được để trống")
    private LocalDateTime saleStart;

    @NotNull(message = "Thời gian đóng bán vé không được để trống")
    private LocalDateTime saleEnd;

    @NotBlank(message = "Tên địa điểm không được để trống")
    private String venueName;

    private String city;
    private String country;
    private String createdBy;
}