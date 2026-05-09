package com.eightthreads.backend.dto.response.admin;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AdminTicketTypeResponse {
    private Long ticketTypeId;
    private String eventId;
    private String name;
    private String type;
    private BigDecimal price;
    private Integer totalQuantity;
    private Integer soldQuantity;
    private Boolean isActive;
}

