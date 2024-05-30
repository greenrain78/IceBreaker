package org.example.IceBreaking.controller;

import lombok.RequiredArgsConstructor;
import org.example.IceBreaking.domain.User;
import org.example.IceBreaking.dto.ValidateUserIdDto;
import org.example.IceBreaking.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final UserRepository userRepository;

    @GetMapping("/showSignup")
    public String showSignupForm() {
        return "/user/signup";
    }

    @PostMapping("/signup")
    public String createUser(@ModelAttribute User user) {
        userRepository.save(user);

        // 검증용
        // 회원가입한 유저들의 정보를 콘솔에 print
        User savedUser = userRepository.findById(user.getUserId());
        System.out.println("savedUser.get().getUserId() = " + savedUser.getUserId());
        System.out.println("savedUser.get().getPassword() = " + savedUser.getPassword());
        System.out.println("savedUser.get().getName() = " + savedUser.getName());
        System.out.println("savedUser.get().getDepartment() = " + savedUser.getDepartment());
        System.out.println("savedUser.get().getStudentId() = " + savedUser.getStudentId());
        System.out.println("savedUser.get().getInterests() = " + Arrays.toString(savedUser.getInterests()));

        return "redirect:/showLogin";         // 회원가입 후 로그인 화면으로 redirect
    }

    @PostMapping("/signup/id-check")
    @ResponseBody
    public ResponseEntity<Boolean> validateUserId(@RequestBody ValidateUserIdDto validateUserIdDto) {
        System.out.println("validateUserIdDto.getUserId() = " + validateUserIdDto.getUserId());
        
        if (userRepository.findById(validateUserIdDto.getUserId()) != null) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
    }

}




