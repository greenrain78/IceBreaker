package com.example.IceBreaking.gpt;

import com.example.IceBreaking.config.AppEnvConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GptCallTest {

    @Autowired
    private AppEnvConfig appEnv;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Disabled
    @Test
    @DisplayName("GPT API 호출 테스트")
    public void testGetGptResponse() {
        WebClient webClient = webClientBuilder.build();
        GptClient gptClient = new GptClient(appEnv, webClient);

        String response = gptClient.getGptResponse("안녕하세요");
        assertNotNull(response);
        System.out.println("Response: " + response);
    }
}