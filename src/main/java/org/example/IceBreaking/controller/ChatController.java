package org.example.IceBreaking.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.IceBreaking.config.AppEnvConfig;
import org.example.IceBreaking.domain.Question;
import org.example.IceBreaking.domain.Team;
import org.example.IceBreaking.domain.User;
import org.example.IceBreaking.dto.WebSocketMessageDto;
import org.example.IceBreaking.repository.chat.ChatRepository;
import org.example.IceBreaking.repository.question.QuestionRepository;
import org.example.IceBreaking.repository.team.TeamRepository;
import org.example.IceBreaking.repository.user.UserRepository;
import org.example.IceBreaking.service.GptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRepository chatRepository;
    private final TeamRepository teamRepository;
    private final HttpSession httpSession;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final GptService gptService;

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
    public ResponseEntity<String> showGptQuestion(@PathVariable("teamId") int teamId) {
        Map<String, Integer> interests = questionRepository.findAllInterestsByTeam(teamId);
        if (interests.isEmpty()) {
            return new ResponseEntity<>("", HttpStatus.OK);
        }
        String prompt = "가중치에 따라 알맞는 주제에 대한 질문을 생성해\n" +
                "사람들이 더 많이 공감하고 대화를 많이 할 수 있는 흥미로운 질문으로 작성해\n" +
                "가중치: " + interests + "\n" +
                "답변은 1~3문장으로 작성하고 답변 이외의 내용은 대답하지마";
        String question = gptService.getGptResponse(prompt);
        return new ResponseEntity<>(question, HttpStatus.OK);
    }
}

