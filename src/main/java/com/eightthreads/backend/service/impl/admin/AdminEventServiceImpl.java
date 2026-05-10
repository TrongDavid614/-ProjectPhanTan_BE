package com.eightthreads.backend.service.impl.admin;

import com.eightthreads.backend.common.enums.EventStatus;
import com.eightthreads.backend.dto.request.admin.AdminEventCreateRequest;
import com.eightthreads.backend.dto.request.admin.AdminEventUpdateRequest;
import com.eightthreads.backend.dto.response.admin.AdminEventResponse;
import com.eightthreads.backend.entity.Event;
import com.eightthreads.backend.entity.User;
import com.eightthreads.backend.repository.EventRepository;
import com.eightthreads.backend.repository.UserRepository;
import com.eightthreads.backend.service.admin.AdminEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public AdminEventResponse createEvent(AdminEventCreateRequest request, String ownerIdentifier) {
        User owner = resolveOwner(ownerIdentifier);

        Event event = Event.builder()
                .eventId("e_" + System.currentTimeMillis())
                .createdBy(owner)
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
        // Defensive: trim and attempt exact lookup first
        String idKey = eventId == null ? null : eventId.trim();
        Event event = null;
        if (idKey != null) {
            event = eventRepository.findById(idKey).orElse(null);
        }

        // Fallback: try case-insensitive match or contains match
        if (event == null && idKey != null) {
            event = eventRepository.findAll().stream()
                    .filter(e -> e.getEventId() != null && (
                            e.getEventId().equalsIgnoreCase(idKey) ||
                            e.getEventId().trim().equalsIgnoreCase(idKey) ||
                            e.getEventId().contains(idKey)
                    ))
                    .findFirst()
                    .orElse(null);
        }

        if (event == null) {
            throw new RuntimeException("Không tìm thấy sự kiện");
        }

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
    public List<AdminEventResponse> getAllEvents(String ownerIdentifier) {
        User owner = resolveOwner(ownerIdentifier);

        return eventRepository.findByCreatedBy_UserId(owner.getUserId()).stream()
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
            return getAllEvents(null);
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

    private User resolveOwner(String ownerIdentifier) {
        String identifier = ownerIdentifier;
        if (identifier == null || identifier.isBlank()) {
            org.springframework.security.core.Authentication authentication =
                    org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && authentication.getName() != null) {
                identifier = authentication.getName();
            }
        }

        if (identifier == null || identifier.isBlank()) {
            throw new RuntimeException("Không xác định được người tạo sự kiện");
        }

        Optional<User> byId = userRepository.findById(identifier);
        if (byId.isPresent()) {
            return byId.get();
        }

        final String lookupIdentifier = identifier;
        return userRepository.findByEmail(lookupIdentifier)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng tạo sự kiện: " + lookupIdentifier));
    }
}
