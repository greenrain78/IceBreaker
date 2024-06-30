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
    private static final int OUTPUT_TOKENS = 100;

    private String getApiUrl() {
        String projectId = "greenrain";
        String location = "europe-west1";
        String modelId = "claude-3-5-sonnet@20240620";

        return String.format("https://%s-aiplatform.googleapis.com/v1/projects/%s/locations/%s/publishers/anthropic/models/%s:streamRawPredict",
                location, projectId, location, modelId);
    }

    @Override
    public String callGptSimple(String instruction, List<GptChatDTO> chatList) {
        List<String> contentList = new ArrayList<>();
        int inputTokens = 0;
        // chatList의 첫번째 요소가 model이면 start 메세지 추가
        if (chatList.getFirst().getUsername().equals("model")) {
            contentList.add("{\"role\":\"user\", \"content\": \"Start\"}");
        }

        for (GptChatDTO chat : chatList) {
            inputTokens += chat.getMessage().length();
            String message = chat.getMessage().replace('\n', ' ');
            if (chat.getUsername().equals("model")) {
                contentList.add("{\"role\":\"assistant\", \"content\": \"" + message + "\"}");
            } else {
                contentList.add("{\"role\":\"user\", \"content\": \"" + message + "\"}");
            }
        }




        String content = String.join(",", contentList);
        String MAX_TOKENS = String.valueOf(inputTokens * 2 + OUTPUT_TOKENS);
        String requestBody = """
                {
                  "anthropic_version": "vertex-2023-10-16",
                  "messages": [%s],
                  "system": "%s",
                  "max_tokens": %s
                }""".formatted(content, instruction, MAX_TOKENS);
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
