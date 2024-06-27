package com.example.IceBreaking.service;

import com.example.IceBreaking.dto.ChatDTO;
import com.example.IceBreaking.dto.TeamDTO;
import com.example.IceBreaking.repository.ChatRepository;
import com.example.IceBreaking.repository.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test") // 'test' 프로파일이 설정되어 있다고 가정
public class ChatServiceTest {
    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRepository chatRepository;

    @AfterEach
    public void tearDown() {
        chatRepository.deleteAll();
    }

    @Test
    @DisplayName("채팅 생성 테스트")
    public void createChatTest() {
        // given
        Long teamId = 1L;
        String username = "사용자 이름";
        String message = "메시지";

        // when
        ChatDTO chatDTO = ChatDTO.builder()
                .teamId(teamId)
                .username(username)
                .message(message)
                .build();
        ChatDTO savedChatDTO = chatService.createChat(chatDTO);

        // then
        List<ChatDTO> chatDTOList = chatService.showChat(1L);
        assert chatDTOList.size() == 1;
        ChatDTO resultChatDTO = chatDTOList.getFirst();
        assert resultChatDTO.getTeamId().equals(teamId);
        assert resultChatDTO.getUsername().equals(username);
        assert resultChatDTO.getMessage().equals(message);

        assert savedChatDTO.getTeamId().equals(teamId);
        assert savedChatDTO.getUsername().equals(username);
        assert savedChatDTO.getMessage().equals(message);
        System.out.println(savedChatDTO.getTime());
        System.out.println(resultChatDTO.getTime());
    }

    @Test
    @DisplayName("채팅 페이지 테스트")
    public void showChatPageTest() {
        // given
        Long teamId = 1L;
        String username = "사용자 이름";
        for (int i = 0; i < 20; i++) {
            ChatDTO chatDTO = ChatDTO.builder()
                    .teamId(teamId)
                    .username(username)
                    .message("메시지" + i)
                    .build();
            chatService.createChat(chatDTO);
        }

        // when
        List<ChatDTO> chatDTOList = chatService.showChatPage(1L, 1);

        // then
        assert chatDTOList.size() == 10;
        for (int i = 0; i < 10; i++) {
            ChatDTO chatDTO = chatDTOList.get(i);
            assert chatDTO.getTeamId().equals(teamId);
            assert chatDTO.getUsername().equals(username);
            assert chatDTO.getMessage().equals("메시지" + (i + 10));
        }

    }
}
