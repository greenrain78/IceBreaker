package org.example.IceBreaking.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.IceBreaking.domain.User;
import org.example.IceBreaking.repository.user.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @GetMapping("/showLogin")
    public String showLoginForm() {
        return "/user/login";
    }

    @PostMapping("/login")
    public String userLogin(@RequestParam("userId") String userId,
                            @RequestParam("password") String password) {

        User loginedUser = userRepository.findById(userId);
        if (loginedUser == null || !loginedUser.matchPassword(password)) {
            // 로그인 실패
            return "/user/login_failed";
        } else {
            // 로그인 성공
            httpSession.setAttribute("loginedUser", loginedUser);
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String userLogout() {
        // 로그아웃 시 세션 제거 후 홈화면으로 redirect
        httpSession.removeAttribute("loginedUser");
        return "redirect:/";
    }


}
