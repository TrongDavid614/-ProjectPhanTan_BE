package com.eightthreads.backend.service;

import com.eightthreads.backend.dto.response.TicketResponse;
import java.util.List;

public interface TicketService {
    List<TicketResponse> getUserTickets(String userId);
}