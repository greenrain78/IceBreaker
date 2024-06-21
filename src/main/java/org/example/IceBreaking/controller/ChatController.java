package org.example.IceBreaking.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.IceBreaking.domain.Question;
import org.example.IceBreaking.domain.Team;
import org.example.IceBreaking.domain.User;
import org.example.IceBreaking.dto.WebSocketMessageDto;
import org.example.IceBreaking.repository.chat.ChatRepository;
import org.example.IceBreaking.repository.question.QuestionRepository;
import org.example.IceBreaking.repository.team.TeamRepository;
import org.example.IceBreaking.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRepository chatRepository;
    private final TeamRepository teamRepository;
    private final HttpSession httpSession;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private int QuestionIndex = 0;

    @GetMapping("/chat/{teamId}")
    public String showChatRoom(@PathVariable("teamId") int teamId, Model model) {
        Optional<Team> team = teamRepository.findById(teamId);
        model.addAttribute("team", team.get());         // Optional에서 Team 객체만을 추출

        User loginedUser = (User) httpSession.getAttribute("loginedUser");
        model.addAttribute("user", loginedUser);

        String teamCreatorId = teamRepository.findTeamCreatorId(teamId);
        System.out.println("teamCreatorId = " + teamCreatorId);
        model.addAttribute("teamCreatorId", teamCreatorId);

        // user가 입장한 chatRoom 정보 저장
        userRepository.saveEnteredChatRoom(loginedUser.getUserId(), team.get());

        return "/chat/chatRoomWebSocket";
    }

    @GetMapping("/api/chatList/{teamId}")
    @ResponseBody
    public ResponseEntity<List<WebSocketMessageDto>> getChatList(@PathVariable("teamId") int teamId) {
        List<WebSocketMessageDto> chatList = chatRepository.findChatList(teamId);
        return new ResponseEntity<>(chatList, HttpStatus.OK);
    }

    @GetMapping("/api/interests/{userId}")
    @ResponseBody
    public ResponseEntity<String[]> getInterests(@PathVariable("userId") String userId) {
        User user = userRepository.findById(userId);
        return new ResponseEntity<>(user.getInterests(), HttpStatus.OK);
    }

    @GetMapping("/api/welcomeQuestion")
    @ResponseBody
    public ResponseEntity<Question> showWelcomeQuestion() {
        Question welcomeQuestion = questionRepository.findWelcomeQuestion();
        return new ResponseEntity<>(welcomeQuestion, HttpStatus.OK);
    }

    @GetMapping("/api/question/{teamId}")
    @ResponseBody
    public ResponseEntity<Question> showQuestion(@PathVariable("teamId") int teamId) {

        Question question = questionRepository.findQuestionByTeamInterests(teamId, QuestionIndex++);
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @GetMapping("/api/question/gpt/{teamId}")
    @ResponseBody
    public ResponseEntity<Question> showGptQuestion(@PathVariable("teamId") int teamId) {

        Question question = questionRepository.findQuestionByTeamInterests(teamId, QuestionIndex++);
        return new ResponseEntity<>(question, HttpStatus.OK);
    }
}

