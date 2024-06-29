package com.example.IceBreaking.gpt;

import com.example.IceBreaking.dto.GptChatDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SonnetClientTest {

    @Autowired
    private SonnetClient sonnetClient;

    @Test
    @DisplayName("callGptSimple 테스트")
    void callGptSimpleTest() {
        // given
        // when
        List<GptChatDTO> chatList = new ArrayList<>();
        chatList.add(new GptChatDTO("user", "안녕하세요! 저녁 메뉴 추천해주세요."));
        String response = sonnetClient.callGptSimple("귀엽고 깜찍하게 답변", chatList);
        // then
        System.out.println(response);
    }
}
