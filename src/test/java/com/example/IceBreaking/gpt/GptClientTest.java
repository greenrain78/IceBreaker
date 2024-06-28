package com.example.IceBreaking.gpt;

import com.example.IceBreaking.config.AppEnvConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebFluxTest(GptClient.class)
public class GptClientTest {

    @Mock
    private AppEnvConfig appEnv;

    @Mock
    private WebClient webClient;
    @Disabled
    @Test
    @DisplayName("testGetGptResponse_success")
    public void testGetGptResponse_success() {
        // given
        GptClient gptClient = new GptClient(appEnv, webClient);

        // when
//        String response = gptClient.callGptSimple("Hello", null);

        // then
//        assertEquals("Hello, world!", response);
    }
}
