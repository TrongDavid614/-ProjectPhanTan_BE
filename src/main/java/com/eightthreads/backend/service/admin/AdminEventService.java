package com.eightthreads.backend.service.admin;

import com.eightthreads.backend.common.enums.EventStatus;
import com.eightthreads.backend.dto.request.admin.AdminEventCreateRequest;
import com.eightthreads.backend.dto.request.admin.AdminEventUpdateRequest;
import com.eightthreads.backend.dto.response.admin.AdminEventResponse;
import java.util.List;

public interface AdminEventService {
    AdminEventResponse createEvent(AdminEventCreateRequest request, String ownerIdentifier);
    AdminEventResponse updateEvent(String eventId, AdminEventUpdateRequest request);
    void deleteEvent(String eventId);
    List<AdminEventResponse> getAllEvents(String ownerIdentifier);
    AdminEventResponse getEventById(String eventId);
    List<AdminEventResponse> searchEvents(String keyword);
    List<AdminEventResponse> filterByStatus(EventStatus status);
}
