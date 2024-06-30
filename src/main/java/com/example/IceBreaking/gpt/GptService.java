package com.example.IceBreaking.gpt;

import com.example.IceBreaking.dto.GptChatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {

    private final SonnetClient gptClient;

    // 관심사 조사
    public String getInterest(List<GptChatDTO> chatList) {
        String instruction = "You are a helpful assistant. Your task is to engage in a conversation to learn more about the user's interests. Ask open-ended questions to understand their hobbies, preferences, and passions. Provide a friendly and welcoming environment for the user to share their interests. Respond in Korean.";
        return gptClient.callGptSimple(instruction, chatList);
    }
}
