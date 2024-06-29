package com.example.IceBreaking.gpt;

import com.example.IceBreaking.config.AppEnvConfig;
import com.example.IceBreaking.dto.GptChatDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SonnetClient implements GptClient{
    private final AppEnvConfig appEnv;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String getApiUrl() {
        String projectId = "greenrain";
        String location = "europe-west1";
        String modelId = "claude-3-5-sonnet@20240620";

        return String.format("https://%s-aiplatform.googleapis.com/v1/projects/%s/locations/%s/publishers/anthropic/models/%s:streamRawPredict",
                location, projectId, location, modelId);
    }
    public String callGptSimple() {
        String requestBody = """
                {
                  "anthropic_version": "vertex-2023-10-16",
                  "messages": [{
                    "role": "user",
                    "content": "안녕하세요! 저녁 메뉴 추천해주세요."
                  }],
                  "max_tokens": 100
                }""";
        log.info("Request body: {}", requestBody);

        String response = getResponse(requestBody);
        return response;
    }

    @Override
    public String callGptSimple(String instruction, List<GptChatDTO> chatList) {
        List<String> contentList = new ArrayList<>();
        for (GptChatDTO chat : chatList) {
            if (chat.getUsername().equals("model")) {
                contentList.add("{\"role\":\"model\", \"content\": \"" + chat.getMessage() + "\"}");
            } else {
                contentList.add("{\"role\":\"user\",  \"content\": \"" + chat.getMessage() + "\"}");
            }
        }
        String content = String.join(",", contentList);
        String requestBody = """
                {
                  "anthropic_version": "vertex-2023-10-16",
                  "messages": [%s],
                  "system": "%s",
                  "max_tokens": 100
                }""".formatted(content, instruction);
        log.info("Request body: {}", requestBody);

        return getResponse(requestBody);
    }

    @Override
    public String getResponse(String json) {
        Mono<String> responseMono = webClient.post()
                .uri(getApiUrl())
                .header("Authorization", "Bearer " + appEnv.getSonnetApiKey())
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(json))
                .retrieve()
                .bodyToMono(String.class);
        String response = responseMono.block();
        // json parsing candidates.parts.text
        try {
            log.debug("Response: {}", response);
            return objectMapper.readTree(response).get("content").get(0).get("text").asText();
        } catch (Exception e) {
            log.error("Parsing Error: {}", e.getMessage());
            log.error("Response: {}", response);
            throw new RuntimeException("Parsing Error");
        }
    }
}
