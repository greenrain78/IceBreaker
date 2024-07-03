package com.example.IceBreaking.gpt;

import com.example.IceBreaking.dto.GptChatDTO;

import java.util.List;

public interface GptClient {

    String callGptSimple(String instruction, List<GptChatDTO> chatList);

    String getResponse(String instruction, String content);

    String getResponse(String requestBody);
}
