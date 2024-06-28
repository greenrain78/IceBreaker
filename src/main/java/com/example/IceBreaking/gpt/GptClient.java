package com.example.IceBreaking.gpt;

import com.example.IceBreaking.config.AppEnvConfig;
import com.example.IceBreaking.dto.GptChatDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptClient {
    private final AppEnvConfig appEnv;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?";

    private String getApiUrl() {
        //        log.info("API URL: {}", API_URL + "key=" + appEnv.getApiKey());
        return API_URL + "key=" + appEnv.getApiKey();
    }
    public String getResponse(String json) {
        log.info("Request: {}", json);
        String response = webClient.post()
                .uri(getApiUrl())
                .bodyValue(json)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // json parsing candidates.parts.text
        try {
            log.debug("Response: {}", response);
            return objectMapper.readTree(response).get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();
        } catch (Exception e) {
            log.error("Parsing Error: {}", e.getMessage());
            log.error("Response: {}", response);
            throw new RuntimeException("Parsing Error");
        }
    }
    public String callGptSimple(String instruction, List<GptChatDTO> chatList) {
        // 시스템 명령 구성
        String systemContent = "{\"parts\":[{\"text\": \"" + instruction + "\"}]}";
        // 채팅 내용 구성
        List<String> contentList = new ArrayList<>();
        for (GptChatDTO chat : chatList) {
            if (Objects.equals(chat.getUsername(), "model")) {
                contentList.add("{\"role\":\"model\", \"parts\":[{\"text\": \"" + chat.getMessage() + "\"}]}");
            } else {
                contentList.add("{\"role\":\"user\", \"parts\":[{\"text\": \"" + chat.getMessage() + "\"}]}");
            }
        }
        // json 구성
        String json = "{\"system_instruction\": " + systemContent +", \"contents\": ["+ String.join(",", contentList) + "]}";
        return getResponse(json);


    }
}
