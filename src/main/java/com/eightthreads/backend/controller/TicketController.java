package com.eightthreads.backend.controller;

import com.eightthreads.backend.dto.response.TicketResponse;
import com.eightthreads.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketResponse>> getMyTickets(@PathVariable("userId") String userId){
        List<TicketResponse> tickets = ticketService.getUserTickets(userId);
        return ResponseEntity.ok(tickets);
    }
}