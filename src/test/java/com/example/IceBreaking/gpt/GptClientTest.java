package com.example.IceBreaking.gpt;

import com.example.IceBreaking.config.AppEnvConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
        String response = gptClient.getGptResponse("Hello");

        // then
        assertEquals("Hello, world!", response);
    }
}
