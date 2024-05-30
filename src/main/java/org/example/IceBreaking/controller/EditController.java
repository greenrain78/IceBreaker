package org.example.IceBreaking.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.IceBreaking.domain.User;
import org.example.IceBreaking.repository.user.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class EditController {

    private final UserRepository userRepository;

    @GetMapping("/user/showEdit")
    public String showEditForm(Model model, HttpSession httpSession) {
        // 세션에서 로그인된 유저 정보 가져오기
        User loginedUser = (User) httpSession.getAttribute("loginedUser");

        // model에 추가하여 view에 전달
        model.addAttribute("user", loginedUser);

        return "/user/edit";
    }

    @PostMapping("/user/edit")
    public String editUserInfo(@ModelAttribute User user, HttpSession httpSession) {
        User editUser = userRepository.changeUserInfo(user);

        // 세션 정보 update
        httpSession.setAttribute("loginedUser", editUser);

        // 수정 정보 확인
        System.out.println("editUser.get().getUserId() = " + editUser.getUserId());
        System.out.println("editUser.get().getPassword() = " + editUser.getPassword());
        System.out.println("editUser.get().getName() = " + editUser.getName());
        System.out.println("editUser.get().getDepartment() = " + editUser.getDepartment());
        System.out.println("editUser.get().getStudentId() = " + editUser.getStudentId());
        System.out.println("Arrays.toString(editUser.getInterests()) = " + Arrays.toString(editUser.getInterests()));
        
        return "redirect:/";            // 홈화면으로 redirect
    }
}
