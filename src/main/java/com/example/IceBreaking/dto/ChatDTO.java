package com.example.IceBreaking.dto;

import com.example.IceBreaking.entity.ChatEntity;
import com.example.IceBreaking.entity.TeamEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChatDTO {
    private Long teamId;
    private String username;
    private String message;
    private LocalDateTime time;

    public ChatEntity toEntity() {
        return ChatEntity.builder()
                .teamId(teamId)
                .userName(username)
                .message(message)
                .build();
    }
    public static ChatDTO of(ChatEntity chatEntity) {
        return new ChatDTO(
                chatEntity.getTeamId(),
                chatEntity.getUserName(),
                chatEntity.getMessage(),
                chatEntity.getTime()
        );
    }
}
