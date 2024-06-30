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

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GptServiceTest {
    @Autowired
    private GptService gptService;
    @Autowired
    private ChatRepository chatRepository;


//    @Disabled
    @Test
    @DisplayName("직접 호출 테스트")
    public void testDirectCall() {
        List<GptChatDTO> chatList = new ArrayList<>();
        chatList.add(new GptChatDTO("model", "안녕하세요! 오늘 하루 어떠셨나요? 요즘 관심 있는 취미나 활동이 있으신가요? 어떤 것들이 여러분의 열정을 불러일으키는지 궁금해요. 자유롭게 이야기해 주세요!"));
        chatList.add(new GptChatDTO("user", "요즘 관심 있는 취미는 없어요."));
        String response = gptService.getInterest(chatList, "5");
        assertNotNull(response);
        System.out.println("Response: " + response);
    }

    @Test
    @DisplayName("대화 내용 분석 테스트")
    public void testAnalyzeChat() {
        List<ChatEntity> chatList = chatRepository.findTop30ByTeamIdOrderByTimeAsc(8L);
        List<GptChatDTO> gptChatList = new ArrayList<>();
        for (ChatEntity chat : chatList) {
            gptChatList.add(new GptChatDTO(chat.getUserName(), chat.getMessage()));
        }
        String response = gptService.analyzeChat(gptChatList);
        System.out.println("Response: " + response);


    }
}