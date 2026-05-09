package com.eightthreads.backend.service.impl.admin;

import com.eightthreads.backend.common.enums.OrderStatus;
import com.eightthreads.backend.dto.response.admin.AdminDashboardResponse;
import com.eightthreads.backend.repository.EventRepository;
import com.eightthreads.backend.repository.OrderRepository;
import com.eightthreads.backend.repository.TicketRepository;
import com.eightthreads.backend.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final EventRepository eventRepository;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    @Override
    public AdminDashboardResponse getDashboardStats() {
        long totalEvents = eventRepository.count();
        long totalOrders = orderRepository.count();
        BigDecimal totalRevenue = orderRepository.sumTotalAmountByStatus(OrderStatus.PAID);
        long totalTicketsSold = ticketRepository.count();

        return AdminDashboardResponse.builder()
                .totalEvents(totalEvents)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue == null ? BigDecimal.ZERO : totalRevenue)
                .totalTicketsSold(totalTicketsSold)
                .build();
    }
}
