package org.example.IceBreaking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.IceBreaking.config.AppEnvConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {
    private final AppEnvConfig appEnv;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?";

    private String getApiUrl() {
        return API_URL + "key=" + appEnv.getApiKey();
    }

    public String getGptResponse(String text) {
        String json = "{\"contents\": [{\"parts\":[{\"text\": \"" + text + "\"}]}]}";
//        log.debug("Request: {}", json);
//        log.debug("API URL: {}", getApiUrl());

        // webClient 요청 데이터를 json parsing -> String
        String response = webClient.post()
                .uri(getApiUrl())
                .bodyValue(json)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // json parsing candidates.parts.text
        try {
            log.debug("Response: {}", response);
            log.error("mapping Error: {}", objectMapper.readTree(response));
            return objectMapper.readTree(response).get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();
        } catch (Exception e) {
            log.error("Parsing Error: {}", e.getMessage());
            log.error("Response: {}", response);

            throw new RuntimeException("Parsing Error");
        }
    }
}
