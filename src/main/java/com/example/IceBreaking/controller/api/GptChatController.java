package com.example.IceBreaking.controller.api;

import com.example.IceBreaking.dto.ChatDTO;
import com.example.IceBreaking.dto.GptChatDTO;
import com.example.IceBreaking.dto.TeamDTO;
import com.example.IceBreaking.entity.ChatEntity;
import com.example.IceBreaking.entity.TeamEntity;
import com.example.IceBreaking.gpt.GptService;
import com.example.IceBreaking.repository.ChatRepository;
import com.example.IceBreaking.repository.TeamRepository;
import com.example.IceBreaking.service.ChatService;
import com.example.IceBreaking.service.ChatSupportService;
import com.example.IceBreaking.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GptChatController {

    private final ChatSupportService chatSupportService;
    private final GptService gptService;
    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final TeamService teamService;
    @GetMapping("/gpt/call/simple/{teamId}")
    public  ResponseEntity<Object> getGptChat(@PathVariable Long teamId) {
        chatSupportService.callGptChat(teamId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/gpt/analyzer/{teamId}")
    public ResponseEntity<Object> getGptAnalyzer(@PathVariable Long teamId) {
        log.info("GPT-3 Analyzer called: {}", teamId);
        String analyzerId = teamService.getSettingValue(teamId, "analyzer_id");
        log.info("analyzer_id: {}", analyzerId);
        // analyzer_id 가 있으면 기존 내용 전달
        if (analyzerId != null) {
            ChatEntity chatEntity = chatRepository.findById(Long.parseLong(analyzerId)).orElseThrow();
            return ResponseEntity.ok(ChatDTO.of(chatEntity));
        }
        List<GptChatDTO> chatList = chatService.showChat(teamId).stream().map(
                chat -> new GptChatDTO(chat.getUsername(), chat.getMessage())
        ).toList();
        String result = gptService.analyzeChat(chatList);
        ChatDTO chatDTO = ChatDTO.builder()
                .teamId(teamId)
                .username("model")
                .message(result)
                .build();
        log.info("GPT-3 Analyzer result: {}", chatDTO);
        ChatEntity chatEntity = chatRepository.save(chatDTO.toEntity());
        teamService.updateSettings(teamId, "analyzer_id", chatEntity.getId().toString());
        return ResponseEntity.ok(ChatDTO.of(chatEntity));
    }
}
