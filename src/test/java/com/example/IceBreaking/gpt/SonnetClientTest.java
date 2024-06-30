package com.example.IceBreaking.gpt;

import com.example.IceBreaking.dto.GptChatDTO;
import com.example.IceBreaking.entity.ChatEntity;
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

//    @Test
//    @DisplayName("callGptSimple 테스트")
//    void callGptSimpleTest2() {
//        // given
//        // when
//        List<GptChatDTO> chatList = new ArrayList<>();
//        chatList.add(new GptChatDTO("model", "안녕하세요! 오늘 하루 어떠셨나요? 요즘 관심 있는 취미나 활동이 있으신가요? 어떤 것들이 여러분의 열정을 불러일으키는지 궁금해요. 자유롭게 이야기해 주세요!"));
//        chatList.add(new GptChatDTO("user", "저녁 메뉴 추천해"));
//        chatList.add(new GptChatDTO("model", "아, 저녁 메뉴 추천이군요! 맛있는 음식에 관심이 있으시네요. 어떤 종류의 음식을 좋아하시나요? 한식, 양식, 중식, 일식 중에서 특별히 선호하는 것이 있나요? 아니면 평소에 자주 먹는 음식이 있으신가요? 그리고 오늘 기분이나 날씨 같은 특별한 요인이 있다면 그에 맞는 음식을 추천해 드릴 수 있을 것 같아요. 예를 들어, 피로한 날엔 따뜻한 국물 요리가 좋을 수 있고, 축하할 일이 있다면 특별한 요리를 추천할 수 있겠죠. 음식 선호도나 오늘의 기분에 대해 조금 더 말씀해 주시면, 그에"));
//        chatList.add(new GptChatDTO("user", "난 한식으로 추천해줘"));
//        String instruction = "You are a helpful assistant. Your task is to engage in a conversation to learn more about the user's interests. Ask open-ended questions to understand their hobbies, preferences, and passions. Provide a friendly and welcoming environment for the user to share their interests. Respond in Korean.";
//        String response = sonnetClient.callGptSimple(instruction, chatList);
//
//        System.out.println(response);
//    }
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
