package com.eightthreads.backend.service.admin;

import com.eightthreads.backend.dto.request.admin.AdminTicketTypeCreateRequest;
import com.eightthreads.backend.dto.request.admin.AdminTicketTypeUpdateRequest;
import com.eightthreads.backend.dto.response.admin.AdminTicketTypeResponse;

import java.util.List;

public interface AdminTicketTypeService {
    AdminTicketTypeResponse createTicketType(AdminTicketTypeCreateRequest request);
    AdminTicketTypeResponse updateTicketType(Long ticketTypeId, AdminTicketTypeUpdateRequest request);
    void deleteTicketType(Long ticketTypeId);
    List<AdminTicketTypeResponse> getTicketTypesByEvent(String eventId);
}

