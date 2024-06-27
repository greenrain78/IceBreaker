package com.example.IceBreaking.handler;

import com.example.IceBreaking.dto.ChatDTO;
import com.example.IceBreaking.dto.ChatSocketDTO;
import com.example.IceBreaking.entity.ChatEntity;
import com.example.IceBreaking.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final SimpMessagingTemplate template;
    private final ChatRepository chatRepository;

    @MessageMapping("/chat/message")
    public void message(ChatSocketDTO chatSocketDTO, SimpMessageHeaderAccessor headerAccessor) {
        log.info("message(message = {})", chatSocketDTO.toString());

        ChatDTO chatDTO = ChatDTO.builder()
                .teamId(chatSocketDTO.getTeamId())
                .username(chatSocketDTO.getUsername())
                .message(chatSocketDTO.getMessage())
                .build();
        // chat 저장

        ChatEntity chatEntity = chatRepository.save(chatDTO.toEntity());
        ChatDTO savedChatDTO = ChatDTO.of(chatEntity);
        template.convertAndSend("/sub/chat/room/" + chatSocketDTO.getTeamId(), savedChatDTO);
    }

}