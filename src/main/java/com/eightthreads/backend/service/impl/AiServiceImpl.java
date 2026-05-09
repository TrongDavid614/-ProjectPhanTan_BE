package com.eightthreads.backend.service.impl;

import com.eightthreads.backend.entity.Event;
import com.eightthreads.backend.repository.EventRepository;
import com.eightthreads.backend.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    @Value("${spring.ai.google.genai.api-key}")
    private String apiKey;

    private final EventRepository eventRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String chatWithAi(String userMessage) {
        List<Event> events = eventRepository.findAll();

        String eventData = events.stream()
                .map(e -> String.format(
                        "- Tên sự kiện: %s | Ngày diễn ra: %s | Địa điểm: %s | Mô tả: %s",
                        e.getName(),
                        e.getStartTime(),
                        e.getVenueName(),
                        e.getDescription()
                ))
                .collect(Collectors.joining("\n"));

        String systemPrompt = """
                Bạn là nhân viên tư vấn bán vé sự kiện thân thiện, chuyên nghiệp của hệ thống 8ThreadsEvent.
                
                Dưới đây là danh sách các sự kiện đang bán vé:
                
                %s
                
                Quy tắc:
                1. Chỉ trả lời dựa trên dữ liệu trên.
                2. Trả lời ngắn gọn, lịch sự.
                3. Luôn mời khách đặt vé.
                4. Nếu không có thông tin, hãy xin lỗi.
                """.formatted(eventData);

        String finalPrompt = systemPrompt + "\n\nKhách hàng: " + userMessage;

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        String requestBody = """
                {
                  "contents": [{
                    "parts": [{
                      "text": "%s"
                    }]
                  }]
                }
                """.formatted(finalPrompt.replace("\"", "\\\""));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}