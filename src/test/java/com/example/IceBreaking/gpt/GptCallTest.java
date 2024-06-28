package com.example.IceBreaking.gpt;

import com.example.IceBreaking.config.AppEnvConfig;
import com.example.IceBreaking.dto.GptChatDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GptCallTest {

    @Autowired
    private AppEnvConfig appEnv;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Disabled
    @Test
    @DisplayName("직접 호출 테스트")
    public void testDirectCall() {
        WebClient webClient = webClientBuilder.build();
        GptClient gptClient = new GptClient(appEnv, webClient);
        String request = "{\"system_instruction\": {\"parts\":[{\"text\": \"귀엽고 깜찍하게 답변해\"}]}, \"contents\": [{\"role\":\"user\", \"parts\":[{\"text\": \"안녕하세요\"}]},{\"role\":\"model\", \"parts\":[{\"text\": \"안녕하세요! 🤗  오늘 하루도 즐겁게 보내고 계신가요?  저는 오늘도 여러분을 위해 귀엽고 깜찍한 답변을 준비하고 있답니다!  😊  무엇을 도와드릴까요? 💖 \"}]}]}";
        String response = gptClient.getResponse(request);
        assertNotNull(response);
        System.out.println("Response: " + response);

    }

//    @Disabled
    @Test
    @DisplayName("GPT API 호출 테스트")
    public void testGetGptResponse() {
        WebClient webClient = webClientBuilder.build();
        GptClient gptClient = new GptClient(appEnv, webClient);

        List<GptChatDTO> chatList = new ArrayList<>();
        GptChatDTO chatDTO = new GptChatDTO("user", "안녕하세요");
        chatList.add(chatDTO);

        String response = gptClient.callGptSimple(chatList);
//        String modelText = "배고파서 저녁먹으려고";

        assertNotNull(response);
        System.out.println("Response: " + response);
    }
    @Test
    @DisplayName("GPT API을 이전 요청과 함께 호출하는 테스트")
    public void testGetGptResponseWithModel() {
        WebClient webClient = webClientBuilder.build();
        GptClient gptClient = new GptClient(appEnv, webClient);

        List<GptChatDTO> chatList = new ArrayList<>();
        GptChatDTO chatDTO = new GptChatDTO("user", "안녕하세요");
        chatList.add(chatDTO);

        // 첫번째 요청
        String modelText = gptClient.callGptSimple(chatList);
        System.out.println("Model Response: " + modelText);
        // 이전 요청에 대한 응답을 다음 요청에 함께 전달
        chatList.add(new GptChatDTO("model", modelText));
        chatList.add(new GptChatDTO("user", "점심을 추천해줘"));
        String response = gptClient.callGptSimple(chatList);

        assertNotNull(response);
        System.out.println("Model Response: " + response);
    }
}