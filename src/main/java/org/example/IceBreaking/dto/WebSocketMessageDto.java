package org.example.IceBreaking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessageDto {
    // 웹 소켓으로 주고받는 message dto

    private int teamId;
    private String userId;
    private String userName;
    private String message;
    private LocalDateTime time;
    private String messageType;         // message vs question
}
