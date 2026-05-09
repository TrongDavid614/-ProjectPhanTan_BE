package com.eightthreads.backend.service.impl.admin;

import com.eightthreads.backend.dto.request.admin.AdminTicketTypeCreateRequest;
import com.eightthreads.backend.dto.request.admin.AdminTicketTypeUpdateRequest;
import com.eightthreads.backend.dto.response.admin.AdminTicketTypeResponse;
import com.eightthreads.backend.entity.Event;
import com.eightthreads.backend.entity.TicketType;
import com.eightthreads.backend.repository.EventRepository;
import com.eightthreads.backend.repository.TicketTypeRepository;
import com.eightthreads.backend.service.admin.AdminTicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTicketTypeServiceImpl implements AdminTicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;
    private final EventRepository eventRepository;

    @Override
    public AdminTicketTypeResponse createTicketType(AdminTicketTypeCreateRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));

        TicketType ticketType = TicketType.builder()
                .event(event)
                .name(request.getName())
                .type(request.getType())
                .price(request.getPrice())
                .totalQuantity(request.getTotalQuantity())
                .soldQuantity(request.getSoldQuantity() == null ? 0 : request.getSoldQuantity())
                .isActive(request.getIsActive())
                .build();

        TicketType saved = ticketTypeRepository.save(ticketType);
        return toResponse(saved);
    }

    @Override
    public AdminTicketTypeResponse updateTicketType(Long ticketTypeId, AdminTicketTypeUpdateRequest request) {
        TicketType ticketType = ticketTypeRepository.findByTicketTypeId(ticketTypeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại vé"));

        if (request.getEventId() != null && !request.getEventId().isBlank()) {
            Event event = eventRepository.findById(request.getEventId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));
            ticketType.setEvent(event);
        }

        ticketType.setName(request.getName());
        ticketType.setType(request.getType());
        ticketType.setPrice(request.getPrice());
        ticketType.setTotalQuantity(request.getTotalQuantity());
        ticketType.setSoldQuantity(request.getSoldQuantity() == null ? 0 : request.getSoldQuantity());
        ticketType.setIsActive(request.getIsActive());

        return toResponse(ticketTypeRepository.save(ticketType));
    }

    @Override
    public void deleteTicketType(Long ticketTypeId) {
        TicketType ticketType = ticketTypeRepository.findByTicketTypeId(ticketTypeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại vé"));
        ticketTypeRepository.delete(ticketType);
    }

    @Override
    public List<AdminTicketTypeResponse> getTicketTypesByEvent(String eventId) {
        return ticketTypeRepository.findByEvent_EventId(eventId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AdminTicketTypeResponse toResponse(TicketType ticketType) {
        return AdminTicketTypeResponse.builder()
                .ticketTypeId(ticketType.getTicketTypeId())
                .eventId(ticketType.getEvent() != null ? ticketType.getEvent().getEventId() : null)
                .name(ticketType.getName())
                .type(ticketType.getType())
                .price(ticketType.getPrice())
                .totalQuantity(ticketType.getTotalQuantity())
                .soldQuantity(ticketType.getSoldQuantity())
                .isActive(ticketType.getIsActive())
                .build();
    }
}

