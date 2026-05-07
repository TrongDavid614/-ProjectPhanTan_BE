package com.eightthreads.backend.service.impl;

import com.eightthreads.backend.dto.response.TicketResponse;
import com.eightthreads.backend.entity.Ticket;
import com.eightthreads.backend.repository.TicketRepository;
import com.eightthreads.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public List<TicketResponse> getUserTickets(String userId) {
        List<Ticket> tickets = ticketRepository.findByOwner_UserId(userId);

        return tickets.stream().map(ticket -> TicketResponse.builder()
                .ticketId(ticket.getTicketId())
                .qrCode(ticket.getQrCode())
                .status(ticket.getStatus())
                .eventName(ticket.getEvent().getName())
                .eventStartTime(ticket.getEvent().getStartTime())
                .eventEndTime(ticket.getEvent().getEndTime())
                .venueName(ticket.getEvent().getVenueName())
                .eventImg(ticket.getEvent().getImg())
                .ticketTypeName(ticket.getTicketType().getName())
                .price(ticket.getTicketType().getPrice())
                .build()
        ).collect(Collectors.toList());
    }
}