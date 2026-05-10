package com.eightthreads.backend.controller.user;

import com.eightthreads.backend.dto.response.user.TicketResponse;
import com.eightthreads.backend.entity.User;
import com.eightthreads.backend.repository.UserRepository;
import com.eightthreads.backend.service.user.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/api/user/tickets", "/api/v1/tickets"})
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserTicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketResponse>> getMyTickets(@PathVariable("userId") String userId){
        List<TicketResponse> tickets = ticketService.getUserTickets(resolveUserId(userId));
        return ResponseEntity.ok(tickets);
    }

    private String resolveUserId(String identifier) {
        Optional<User> byId = userRepository.findById(identifier);
        if (byId.isPresent()) {
            return byId.get().getUserId();
        }

        return userRepository.findByEmail(identifier)
                .map(User::getUserId)
                .orElse(identifier);
    }
}
