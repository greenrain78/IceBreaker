package com.example.IceBreaking.controller;

import com.example.IceBreaking.dto.TeamCreateDTO;
import com.example.IceBreaking.dto.TeamDTO;
import com.example.IceBreaking.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/team/create")
    public ResponseEntity<Object> createTeam(@RequestBody TeamCreateDTO teamCreateDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TeamDTO teamDTO = teamService.createTeam(teamCreateDTO.getTeamName(), username);
        return ResponseEntity.ok(teamDTO);
    }
    @GetMapping("/team/join")
    public String joinTeam(Long teamId, Model model) {
        return "redirect:/team";
    }
}
