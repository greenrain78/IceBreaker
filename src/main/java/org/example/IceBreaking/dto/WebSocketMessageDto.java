package org.example.IceBreaking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessageDto {
    private int teamId;
    private String userId;
    private String userName;
    private String message;
    private LocalDateTime time;
    private String messageType;         // message vs question
}
