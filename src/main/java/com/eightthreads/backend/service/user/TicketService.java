package com.eightthreads.backend.service.user;

import com.eightthreads.backend.dto.response.user.TicketResponse;
import java.util.List;

public interface TicketService {
    List<TicketResponse> getUserTickets(String userId);
}
