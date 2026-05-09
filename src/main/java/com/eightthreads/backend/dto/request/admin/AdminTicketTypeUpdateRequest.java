package com.eightthreads.backend.dto.request.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminTicketTypeUpdateRequest {

    @NotBlank(message = "Event ID không được để trống")
    private String eventId;

    @NotBlank(message = "Tên loại vé không được để trống")
    private String name;

    private String type;

    @NotNull(message = "Giá vé không được để trống")
    private BigDecimal price;

    @NotNull(message = "Tổng số lượng không được để trống")
    @Min(value = 0, message = "Tổng số lượng phải >= 0")
    private Integer totalQuantity;

    @Min(value = 0, message = "Số lượng đã bán phải >= 0")
    private Integer soldQuantity;

    @NotNull(message = "Trạng thái kích hoạt không được để trống")
    private Boolean isActive;
}

