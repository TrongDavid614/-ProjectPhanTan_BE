package com.eightthreads.backend.service.user;

import com.eightthreads.backend.dto.request.user.EventCreateRequest;
import com.eightthreads.backend.entity.Event;
import com.eightthreads.backend.entity.TicketType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    Event createEvent(EventCreateRequest request, MultipartFile imageFile);

    List<Event> getAllEvents();
    List<TicketType> getTicketTypesByEventId(String eventId);
}
