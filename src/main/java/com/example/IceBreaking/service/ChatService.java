package com.example.IceBreaking.service;

import com.example.IceBreaking.dto.ChatDTO;
import com.example.IceBreaking.entity.ChatEntity;
import com.example.IceBreaking.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    public static final int PAGE_SIZE = 10;

    @Transactional
    public ChatDTO createChat(ChatDTO chatDTO) {
        ChatEntity chatEntity = chatRepository.save(chatDTO.toEntity());
        return ChatDTO.of(chatEntity);
    }

    @Transactional
    public List<ChatDTO> showChat(Long teamId) {
        List<ChatEntity> chatEntityList = chatRepository.findByTeamId(teamId);
        return chatEntityList.stream()
                .map(ChatDTO::of)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<ChatDTO> showChatPage(Long teamId, int page) {
        List<ChatEntity> chatEntityList = chatRepository.findByTeamId(teamId);
        return chatEntityList.stream()
                .skip((long) page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .map(ChatDTO::of)
                .collect(Collectors.toList());
    }
}
