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
        // basic 팀은 GPT-3 API 호출하지 않음
        if (teamEntity.getTeamType().equals("basic")) {
            return;
        }
        // 이전 대화 내용 가져오기
        List<GptChatDTO> chatList = new ArrayList<>();
        chatRepository.findTop30ByTeamIdOrderByTimeAsc(teamId).forEach(
                chatEntity -> chatList.add(new GptChatDTO(chatEntity.getUserName(), chatEntity.getMessage()))
        );
        log.info("chatList: {}", chatList);

        if (isRunningGptChat.get(teamId) == null || !isRunningGptChat.get(teamId)) {
            try {
                isRunningGptChat.put(teamId, true);
                if (teamEntity.getTeamType().equals("welcome")) {
                    welcomeGptChat(teamEntity, chatList);
                }
            } finally {
                isRunningGptChat.put(teamId, false);
            }
        }
    }
    private void welcomeGptChat(TeamEntity teamEntity, List<GptChatDTO> chatList) {
        String gptLimit = teamEntity.getSettings().get("gptLimit");
        String response = gptService.getInterest(chatList, gptLimit);
        sendGptChat(teamEntity.getId(), response);
//        gptLimit 가 0이면 대화 종료
        if (gptLimit.equals("0")) {
            teamRepository.save(teamEntity);
        } else {
            teamEntity.getSettings().put("gptLimit", String.valueOf(Integer.parseInt(gptLimit) - 1));
            teamRepository.save(teamEntity);
        }
    }
    private void sendGptChat(Long teamId, String response) {
        ChatEntity chatEntity = ChatEntity.builder()
                .teamId(teamId)
                .userName("model")
                .message(response)
                .build();
        chatRepository.save(chatEntity);
        template.convertAndSend("/sub/chat/room/" + teamId, ChatDTO.of(chatEntity));
    }
}
