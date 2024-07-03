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
        StringBuilder chatContent = new StringBuilder();
        String lastUsername = "user"; // 첫 대화는 항상 user 로 해야 한다고 api 문서에 명시되어 있음
        StringBuilder content = new StringBuilder();

        // chatList의 첫번째 요소가 model이면 start 메세지 추가
        if (chatList.getFirst().getUsername().equals("model")) {
            content.append("Start");
        }
        for (GptChatDTO chat : chatList) {
            String message = chat.getMessage();
            String username;
            if (chat.getUsername().equals("model")) {
                username = "assistant";
            } else {
                username = "user";
            }
            // 이전 내용과 username이 동일하면 이전 내용에 이어서 추가
            if (lastUsername.equals(username)) {
                content.append(message);
                continue;
            }
            // 이전 내용과 username이 다르면 이전 내용을 contentList에 추가
            chatContent.append("{\"role\":\"").append(lastUsername).append("\", \"content\": \"").append(content).append("\"}, ");
            lastUsername = username;
            content = new StringBuilder();
            content.append(message);
        }
        chatContent.append("{\"role\":\"").append(lastUsername).append("\", \"content\": \"").append(content).append("\"}");
        String requestBody = getRequestBody(instruction, chatContent.toString());
        return getResponse(requestBody);
    }

    @Override
    public String getResponse(String instruction, String content) {
        return null;
    }

    @Override
    public String getResponse(String requestBody) {
        log.info("Request body: {}", requestBody);

        // api 호출
        Mono<String> responseMono = webClient.post()
                .uri(getApiUrl())
                .header("Authorization", "Bearer " + appEnv.getSonnetApiKey())
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(requestBody))
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

    private static String getRequestBody(String instruction, String content) {
        int inputTokens = content.length();
        String reformattedContent = content.replace('\n', ' ').replace("\"", " ");
        // requestBody 생성
        String MAX_TOKENS = String.valueOf(inputTokens * 2 + OUTPUT_TOKENS);
        return """
                {
                  "anthropic_version": "vertex-2023-10-16",
                  "messages": [%s],
                  "system": "%s",
                  "max_tokens": %s
                }""".formatted(reformattedContent, instruction, MAX_TOKENS);
    }
}
