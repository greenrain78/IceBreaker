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
}
