package com.eightthreads.backend.service.impl.admin;

import com.eightthreads.backend.common.enums.OrderStatus;
import com.eightthreads.backend.dto.response.admin.AdminDashboardResponse;
import com.eightthreads.backend.entity.User;
import com.eightthreads.backend.repository.EventRepository;
import com.eightthreads.backend.repository.OrderRepository;
import com.eightthreads.backend.repository.TicketRepository;
import com.eightthreads.backend.repository.UserRepository;
import com.eightthreads.backend.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final EventRepository eventRepository;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    public AdminDashboardResponse getDashboardStats(String ownerIdentifier) {
        User owner = resolveOwner(ownerIdentifier);

        long totalEvents = eventRepository.countByCreatedBy_UserId(owner.getUserId());
        long totalOrders = orderRepository.countByEvent_CreatedBy_UserId(owner.getUserId());
        BigDecimal totalRevenue = orderRepository.sumTotalAmountByStatusAndEventCreatedBy_UserId(
                OrderStatus.PAID,
                owner.getUserId()
        );
        long totalTicketsSold = ticketRepository.countByEvent_CreatedBy_UserId(owner.getUserId());

        return AdminDashboardResponse.builder()
                .totalEvents(totalEvents)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue == null ? BigDecimal.ZERO : totalRevenue)
                .totalTicketsSold(totalTicketsSold)
                .build();
    }

    private User resolveOwner(String ownerIdentifier) {
        String identifier = ownerIdentifier;
        if (!StringUtils.hasText(identifier)) {
            var authentication = org.springframework.security.core.context.SecurityContextHolder
                    .getContext()
                    .getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && StringUtils.hasText(authentication.getName())) {
                identifier = authentication.getName();
            }
        }

        if (!StringUtils.hasText(identifier)) {
            throw new RuntimeException("Không xác định được người dùng dashboard");
        }

        Optional<User> byId = userRepository.findById(identifier);
        if (byId.isPresent()) {
            return byId.get();
        }

        final String lookupIdentifier = identifier;
        return userRepository.findByEmail(lookupIdentifier)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng dashboard: " + lookupIdentifier));
    }
}
