package org.example.IceBreaking.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.IceBreaking.domain.Team;
import org.example.IceBreaking.domain.User;
import org.example.IceBreaking.repository.user.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HttpSession httpSession;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        // 로그인한 user가 입장한 적이 있는 채팅방들을 home화면에 보여주기
        User loginedUser = (User) httpSession.getAttribute("loginedUser");

        if (loginedUser != null) {
            // user가 입장한 채팅방들 목록 get
            List<Team> enteredTeamList = userRepository.findEnteredChatRoom(loginedUser.getUserId());
            model.addAttribute("enteredTeamList", enteredTeamList);
        }

        return "home";
    }

}
