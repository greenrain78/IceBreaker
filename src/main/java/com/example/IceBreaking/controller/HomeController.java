package com.example.IceBreaking.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "home", description = "home controller")
public class HomeController {
    @GetMapping("/")
    @Operation(summary = "home controller")
    public String home(Model model) {
        log.info("home controller" + model);
        return "home";
    }
    @GetMapping("/admin")
    @Operation(summary = "admin controller")
    public String admin(Model model) {
        log.info("admin controller" + model);
        return "admin";
    }
}
