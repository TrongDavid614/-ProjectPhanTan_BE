package com.eightthreads.backend.repository;

import com.eightthreads.backend.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, String> {
    List<TicketType> findByEvent_EventId(String eventId);
    Optional<TicketType> findByTicketTypeId(Long ticketTypeId);
}
