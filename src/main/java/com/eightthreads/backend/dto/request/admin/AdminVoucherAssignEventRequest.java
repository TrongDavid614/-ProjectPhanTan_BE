package com.eightthreads.backend.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminVoucherAssignEventRequest {

    @NotBlank(message = "Event ID không được để trống")
    private String eventId;
}

