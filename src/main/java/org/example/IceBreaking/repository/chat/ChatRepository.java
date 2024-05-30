package org.example.IceBreaking.repository.chat;

import org.example.IceBreaking.dto.WebSocketMessageDto;

import java.util.List;

public interface ChatRepository {

    void saveChat(int teamId, WebSocketMessageDto chat);
    List<WebSocketMessageDto> findChatList(int teamId);
}
