package com.example.IceBreaking.service;

import com.example.IceBreaking.dto.ChatDTO;
import com.example.IceBreaking.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamSupportService {

    private final ChatRepository chatRepository;
    public void initWelcomeTeam(Long teamId) {
        String botMessage = "안녕하세요! 오늘 하루 어떠셨나요? 요즘 관심 있는 취미나 활동이 있으신가요? 어떤 것들이 여러분의 열정을 불러일으키는지 궁금해요. 자유롭게 이야기해 주세요!";
        ChatDTO chatDTO = ChatDTO.builder()
                .teamId(teamId)
                .username("model")
                .message(botMessage)
                .build();
        chatRepository.save(chatDTO.toEntity());
    }
}
