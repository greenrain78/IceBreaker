package com.example.IceBreaking.service;

import com.example.IceBreaking.dto.ChatDTO;
import com.example.IceBreaking.dto.GptChatDTO;
import com.example.IceBreaking.entity.ChatEntity;
import com.example.IceBreaking.entity.TeamEntity;
import com.example.IceBreaking.gpt.GptService;
import com.example.IceBreaking.repository.ChatRepository;
import com.example.IceBreaking.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableAsync
public class ChatSupportService {
    private final SimpMessagingTemplate template;

    private final ChatRepository chatRepository;
    private final GptService gptService;
    private final TeamRepository teamRepository;
    private final Map<Long, Boolean> isRunningGptChat = new HashMap<>();

    @Async
    public void callGptChat(Long teamId) {
        TeamEntity teamEntity = teamRepository.findById(teamId).orElseThrow();
        if (teamEntity.getTeamType().equals("basic")) {
            return;
        }
        // 이전 대화 내용 가져오기
        List<GptChatDTO> chatList = new ArrayList<>();
        chatRepository.findTop30ByTeamIdOrderByTimeDesc(teamId).forEach(chatEntity -> {
            chatList.add(new GptChatDTO(chatEntity.getUserName(), chatEntity.getMessage()));
        });
        log.info("chatList: {}", chatList);

        if (isRunningGptChat.get(teamId) == null || !isRunningGptChat.get(teamId)) {
            try {
                isRunningGptChat.put(teamId, true);

                // GPT-3 API 호출
                log.info("GPT-3 API 호출 중입니다. 잠시만 기다려주세요.");
                String response = gptService.getInterest(chatList);
                log.info("GPT-3 API 호출 완료: {}", response);
                ChatEntity chatEntity = ChatEntity.builder()
                        .teamId(teamId)
                        .userName("model")
                        .message(response)
                        .build();
                chatRepository.save(chatEntity);
                template.convertAndSend("/sub/chat/room/" + teamId, ChatDTO.of(chatEntity));

            } finally {
                isRunningGptChat.put(teamId, false);
            }
        }

    }
}
