package com.example.IceBreaking.gpt;

import com.example.IceBreaking.dto.GptChatDTO;
import com.example.IceBreaking.entity.ChatEntity;
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
    public String getInterest(List<GptChatDTO> chatList, String gptLimit) {
        // gptLimit 가 2 이상 인경우
        String tempGptLimit;
        if (Integer.parseInt(gptLimit) > 1) {
            tempGptLimit = "You have 100 exchanges left."; // 이렇게 안하면 대화횟수 3일때 마무리를 하려고 함
        } else {
            tempGptLimit = "You have %s exchanges left.".formatted(gptLimit);
        }
        String limitMsg = "%s Once the count reaches 0, regardless of the user's response, thank the user for the conversation and conclude the chat.".formatted(tempGptLimit);
        String basicInstruction = "You are a helpful assistant. Your task is to engage in a conversation to learn more about the user's interests. Ask open-ended questions to understand their hobbies, preferences, and passions. Provide a friendly and welcoming environment for the user to share their interests. Respond in Korean.";

        String instruction = basicInstruction + " " + String.format(limitMsg, gptLimit);
        return gptClient.callGptSimple(instruction, chatList);
    }
    public String analyzeChat(List<GptChatDTO> chatList) {
        StringBuilder chatResult = new StringBuilder();
        for (GptChatDTO chat : chatList) {
            chatResult.append(chat.getUsername()).append(": ").append(chat.getMessage()).append("    ");
        }
        String instruction = "Analyze the conversation habits and interests of user 123 based on the given conversation logs. Provide insights into their communication style, preferences, and any notable patterns. Respond in Korean.";
        String content = "{\"role\":\"user\", \"content\": \"" + chatResult + "\"}";
        return gptClient.getResponse(instruction, content);
    }
}
