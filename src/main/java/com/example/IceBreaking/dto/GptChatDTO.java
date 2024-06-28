package com.example.IceBreaking.dto;

import com.example.IceBreaking.entity.ChatEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GptChatDTO {
    private String username;
    private String message;
}
