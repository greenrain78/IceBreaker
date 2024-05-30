package org.example.IceBreaking.webSocketHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.IceBreaking.dto.WebSocketMessageDto;
import org.example.IceBreaking.dto.UserInterestDto;
import org.example.IceBreaking.repository.chat.ChatRepository;
import org.example.IceBreaking.repository.question.QuestionRepository;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final SimpMessagingTemplate template;
    private final ChatRepository chatRepository;
    private final QuestionRepository questionRepository;

    private final static String chatBot = "IceBreaker";
    private final static String messageType = "message";
    private final static String questionType = "question";

    @MessageMapping("/chat/enter")
    public void enter(WebSocketMessageDto connectDto, SimpMessageHeaderAccessor headerAccessor) {
        connectDto.setMessage(connectDto.getUserName() + "님이 입장하였습니다.");
        connectDto.setTime(LocalDateTime.now());
        connectDto.setMessageType(messageType);

        // chat 저장
        chatRepository.saveChat(connectDto.getTeamId(), connectDto);

        // SimpMessageHeaderAccessor에 teamId, userName, userId 추가
        headerAccessor.getSessionAttributes().put("teamId", connectDto.getTeamId());
        headerAccessor.getSessionAttributes().put("userName", connectDto.getUserName());
        headerAccessor.getSessionAttributes().put("userId", connectDto.getUserId());

        template.convertAndSend("/sub/chat/room/" + connectDto.getTeamId(), connectDto);
    }

    @MessageMapping("/chat/message")
    public void message(WebSocketMessageDto message) {
        log.info("message(message = {})", message.toString());

        message.setTime(LocalDateTime.now());
        message.setMessageType(messageType);

        // chat 저장
        chatRepository.saveChat(message.getTeamId(), message);

        template.convertAndSend("/sub/chat/room/" + message.getTeamId(), message);
    }

    @MessageMapping("/chat/question")
    public void question(WebSocketMessageDto question) {
        log.info("question(question = {})", question.toString());

        question.setUserId(chatBot);
        question.setUserName(chatBot);
        question.setTime(LocalDateTime.now());
        question.setMessageType(questionType);

        // chat 저장
        chatRepository.saveChat(question.getTeamId(), question);

        template.convertAndSend("/sub/chat/room/" + question.getTeamId(), question);
    }

    @MessageMapping("/interests")
    public void getTeamInterests(UserInterestDto userInterestDto) {
        log.info("getTeamInterests(userInterestDto = {})", userInterestDto.toString());

        // userInterests 값을 받아서 팀별로 구분해 서버에 저장
        questionRepository.saveInterestsByTeam(userInterestDto);
    }

    @EventListener
    public void chatRoomExitListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        int teamId = (int) headerAccessor.getSessionAttributes().get("teamId");
        String userName = (String) headerAccessor.getSessionAttributes().get("userName");
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");

        // chat 저장
        WebSocketMessageDto exitChat = new WebSocketMessageDto();
        exitChat.setTeamId(teamId);
        exitChat.setUserId(userId);
        exitChat.setUserName(userName);
        exitChat.setMessage(userName + "님이 나갔습니다.");
        exitChat.setTime(LocalDateTime.now());
        exitChat.setMessageType(messageType);
        chatRepository.saveChat(teamId, exitChat);

        // message send
        template.convertAndSend("/sub/chat/room/" + exitChat.getTeamId(), exitChat);
    }

}
