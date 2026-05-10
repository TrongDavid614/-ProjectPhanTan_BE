package com.eightthreads.backend.controller.user;

import com.eightthreads.backend.dto.request.user.EventCreateRequest;
import com.eightthreads.backend.dto.response.ApiResponse;
import com.eightthreads.backend.entity.Event;
import com.eightthreads.backend.entity.TicketType;
import com.eightthreads.backend.service.user.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user/events")
@RequiredArgsConstructor
public class UserEventController {

    private final EventService eventService;

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<Event>> createEvent(
            @Valid @RequestPart("data") EventCreateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {


        Event savedEvent = eventService.createEvent(request, imageFile);

        ApiResponse<Event> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Tạo sự kiện thành công!",
                savedEvent
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<Event>>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();

        ApiResponse<List<Event>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lấy danh sách sự kiện thành công!",
                events
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{eventId}/ticket-types")
    public ResponseEntity<ApiResponse<List<TicketType>>> getTicketTypesByEvent(@PathVariable String eventId) {
        List<TicketType> ticketTypes = eventService.getTicketTypesByEventId(eventId);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lấy danh sách loại vé thành công!",
                ticketTypes
        ));
    }
}
