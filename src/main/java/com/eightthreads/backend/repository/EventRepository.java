package com.eightthreads.backend.repository;
import com.eightthreads.backend.common.enums.EventStatus;
import com.eightthreads.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    List<Event> findByNameContainingIgnoreCase(String keyword);
    List<Event> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameKeyword, String descriptionKeyword);
    List<Event> findByStatus(EventStatus status);
}