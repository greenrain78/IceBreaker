package com.example.IceBreaking.controller;

import com.example.IceBreaking.dto.ChatCreateDTO;
import com.example.IceBreaking.dto.ChatDTO;
import com.example.IceBreaking.dto.TeamCreateDTO;
import com.example.IceBreaking.dto.TeamDTO;
import com.example.IceBreaking.service.ChatService;
import com.example.IceBreaking.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat/create/{teamId}")
    public ResponseEntity<Object> createChat(@PathVariable Long teamId, @RequestBody ChatCreateDTO chatCreateDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatDTO chatDTO = ChatDTO.builder()
                .teamId(teamId)
                .username(username)
                .message(chatCreateDTO.getMessage())
                .build();
        ChatDTO savedChatDTO = chatService.createChat(chatDTO);
        return ResponseEntity.ok(savedChatDTO);
    }

    @GetMapping("/chat/show/{teamId}")
    public ResponseEntity<Object> showChat(@PathVariable Long teamId) {
        return ResponseEntity.ok(chatService.showChat(teamId));
    }
    @GetMapping("/chat/show/{teamId}/{page}")
    public ResponseEntity<Object> showChat(@PathVariable Long teamId, @PathVariable int page) {
        return ResponseEntity.ok(chatService.showChatPage(teamId, page));
    }

}
