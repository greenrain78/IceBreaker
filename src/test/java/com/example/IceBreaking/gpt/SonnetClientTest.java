package com.example.IceBreaking.gpt;

import com.example.IceBreaking.dto.GptChatDTO;
import com.example.IceBreaking.repository.ChatRepository;
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

    @Autowired
    private ChatRepository chatRepository;

    @Test
    @DisplayName("callGptSimple 테스트")
    void callGptSimpleTest() {
        // given
        // when
        List<GptChatDTO> chatList = new ArrayList<>();
        chatList.add(new GptChatDTO("model", "안녕하세요! 오늘 하루 어떠셨나요? 요즘 관심 있는 취미나 활동이 있으신가요? 어떤 것들이 여러분의 열정을 불러일으키는지 궁금해요. 자유롭게 이야기해 주세요!"));
        chatList.add(new GptChatDTO("user", "저는 요즘 취미로 요리를 시작했어요. 요리를 하면서 새로운 요리법을 배우는 것이 즐겁고 행복해요."));
        String instruction = "You are a helpful assistant. Your task is to engage in a conversation to learn more about the user's interests. Ask open-ended questions to understand their hobbies, preferences, and passions. Provide a friendly and welcoming environment for the user to share their interests. Respond in Korean.";
        String response = sonnetClient.callGptSimple(instruction, chatList);
        // then
        System.out.println(response);
    }

@Test
    @DisplayName("callGptSimple 테스트")
    void callGptSimpleTest2() {
        // given
        // when
        Long teamId = 1L;
        List<GptChatDTO> chatList = new ArrayList<>();
        chatRepository.findTop30ByTeamIdOrderByTimeAsc(teamId).forEach(chatEntity -> {
            chatList.add(new GptChatDTO(chatEntity.getUserName(), chatEntity.getMessage()));
        });
//        System.out.println(chatList);
        String instruction = "You are a helpful assistant. Your task is to engage in a conversation to learn more about the user's interests. Ask open-ended questions to understand their hobbies, preferences, and passions. Provide a friendly and welcoming environment for the user to share their interests. Respond in Korean.";
        String response = sonnetClient.callGptSimple(instruction, chatList);
        System.out.println(response);
    }
}
