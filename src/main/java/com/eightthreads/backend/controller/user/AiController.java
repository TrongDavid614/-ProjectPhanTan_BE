package com.eightthreads.backend.controller.user;

import com.eightthreads.backend.service.user.AiService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/chat")
    public ResponseEntity<AiChatResponse> chat(@Valid @RequestBody AiChatRequest request) {
        String reply = aiService.chatWithAi(request.message());
        return ResponseEntity.ok(new AiChatResponse(reply));
    }

    public record AiChatRequest(
            @NotBlank(message = "Message không được để trống")
            String message
    ) {}

    public record AiChatResponse(String reply) {}
}