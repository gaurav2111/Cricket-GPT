package com.cricket.matchdata.openai;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeminiClient {

    private static final String API_KEY = "YOUR_GEMINI_API_KEY";
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY;

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // Request payload
        Map<String, Object> message = new HashMap<>();
        message.put("parts", List.of(Map.of("text", "Tell me a fun fact about space")));
        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", List.of(message));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(ENDPOINT, request, String.class);


    }
}