package com.eightthreads.backend.controller.user;

import com.eightthreads.backend.dto.response.user.TicketResponse;
import com.eightthreads.backend.service.user.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/tickets")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserTicketController {

    private final TicketService ticketService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketResponse>> getMyTickets(@PathVariable("userId") String userId){
        List<TicketResponse> tickets = ticketService.getUserTickets(userId);
        return ResponseEntity.ok(tickets);
    }
}
