package com.example.IceBreaking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSocketDTO {
    private Long teamId;
    private String username;
    private String message;
}
