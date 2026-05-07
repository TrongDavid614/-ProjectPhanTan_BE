package com.eightthreads.backend.service.impl;

import com.eightthreads.backend.dto.request.EventCreateRequest;
import com.eightthreads.backend.entity.Event;
import com.eightthreads.backend.entity.TicketType;
import com.eightthreads.backend.entity.User;
import com.eightthreads.backend.repository.EventRepository;
import com.eightthreads.backend.repository.TicketTypeRepository;
import com.eightthreads.backend.repository.UserRepository;
import com.eightthreads.backend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CloudinaryServiceImpl cloudinaryService;
    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;

    @Override
    public Event createEvent(EventCreateRequest request, MultipartFile imageFile) {
        String imageUrl = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                imageUrl = cloudinaryService.uploadImage(imageFile);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi upload ảnh sự kiện: " + e.getMessage());
            }
        }

        User creator = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy User với ID: " + request.getCreatedBy()));

        Event newEvent = Event.builder()
                .eventId("e_" + System.currentTimeMillis())
                .name(request.getName())
                .categoryId(request.getCategoryId())
                .description(request.getDescription())
                .img(imageUrl)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .saleStart(request.getSaleStart())
                .saleEnd(request.getSaleEnd())
                .venueName(request.getVenueName())
                .city(request.getCity())
                .country(request.getCountry())
                .status("active")
                .createdBy(creator)
                .build();

        return eventRepository.save(newEvent);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<TicketType> getTicketTypesByEventId(String eventId) {
        return ticketTypeRepository.findByEvent_EventId(eventId);
    }
}