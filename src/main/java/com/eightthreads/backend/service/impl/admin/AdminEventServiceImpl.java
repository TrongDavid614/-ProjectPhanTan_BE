package com.eightthreads.backend.service.impl.admin;

import com.eightthreads.backend.common.enums.EventStatus;
import com.eightthreads.backend.dto.request.admin.AdminEventCreateRequest;
import com.eightthreads.backend.dto.request.admin.AdminEventUpdateRequest;
import com.eightthreads.backend.dto.response.admin.AdminEventResponse;
import com.eightthreads.backend.entity.Event;
import com.eightthreads.backend.repository.EventRepository;
import com.eightthreads.backend.service.admin.AdminEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;

    @Override
    public AdminEventResponse createEvent(AdminEventCreateRequest request) {
        Event event = Event.builder()
                .eventId("e_" + System.currentTimeMillis())
                .name(request.getName())
                .categoryId(request.getCategoryId())
                .description(request.getDescription())
                .img(request.getImg())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .saleStart(request.getSaleStart())
                .saleEnd(request.getSaleEnd())
                .venueName(request.getVenueName())
                .city(request.getCity())
                .country(request.getCountry())
                .status(request.getStatus())
                .build();

        Event saved = eventRepository.save(event);
        return toResponse(saved);
    }

    @Override
    public AdminEventResponse updateEvent(String eventId, AdminEventUpdateRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));

        event.setName(request.getName());
        event.setCategoryId(request.getCategoryId());
        event.setDescription(request.getDescription());
        event.setImg(request.getImg());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setSaleStart(request.getSaleStart());
        event.setSaleEnd(request.getSaleEnd());
        event.setVenueName(request.getVenueName());
        event.setCity(request.getCity());
        event.setCountry(request.getCountry());
        event.setStatus(request.getStatus());

        return toResponse(eventRepository.save(event));
    }

    @Override
    public void deleteEvent(String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));
        eventRepository.delete(event);
    }

    @Override
    public List<AdminEventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdminEventResponse getEventById(String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));
        return toResponse(event);
    }

    @Override
    public List<AdminEventResponse> searchEvents(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllEvents();
        }
        return eventRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword.trim(), keyword.trim())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminEventResponse> filterByStatus(EventStatus status) {
        return eventRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    private AdminEventResponse toResponse(Event event) {
        return AdminEventResponse.builder()
                .eventId(event.getEventId())
                .name(event.getName())
                .categoryId(event.getCategoryId())
                .description(event.getDescription())
                .img(event.getImg())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .saleStart(event.getSaleStart())
                .saleEnd(event.getSaleEnd())
                .venueName(event.getVenueName())
                .city(event.getCity())
                .country(event.getCountry())
                .status(event.getStatus())
                .build();
    }
}
