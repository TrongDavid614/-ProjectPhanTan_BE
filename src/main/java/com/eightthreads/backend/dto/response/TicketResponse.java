package com.eightthreads.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponse {
    private String ticketId;
    private String qrCode;
    private String status;
    private String eventName;
    private LocalDateTime eventStartTime;
    private LocalDateTime eventEndTime;
    private String venueName;
    private String eventImg;

    private String ticketTypeName;
    private BigDecimal price;
}