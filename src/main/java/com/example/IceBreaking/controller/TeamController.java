package com.example.IceBreaking.controller;

import com.example.IceBreaking.dto.TeamDTO;
import com.example.IceBreaking.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    @GetMapping("/team")
    public String team(Model model) {
        return "team";
    }
    @PostMapping("/team/create")
    public String createTeam(Model model) {
        return "redirect:/team";
    }
    @GetMapping("/team/join")
    public String joinTeam(Long teamId, Model model) {
        return "redirect:/team";
    }
}
