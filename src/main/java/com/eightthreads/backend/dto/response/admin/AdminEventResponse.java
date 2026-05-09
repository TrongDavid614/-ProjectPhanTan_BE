package com.eightthreads.backend.dto.response.admin;

import com.eightthreads.backend.common.enums.EventStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminEventResponse {
    private String eventId;
    private String name;
    private String categoryId;
    private String description;
    private String img;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime saleStart;
    private LocalDateTime saleEnd;
    private String venueName;
    private String city;
    private String country;
    private EventStatus status;
}

