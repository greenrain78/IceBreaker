package com.example.IceBreaking.controller;


import com.example.IceBreaking.dto.TeamDTO;
import com.example.IceBreaking.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "home", description = "home controller")
public class PageController {
    private final TeamService teamService;
    @GetMapping("/")
    @Operation(summary = "home controller")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        model.addAttribute("role", role);
        model.addAttribute("name", authentication.getName());
        log.info("home controller" + model);
        return "home";
    }
    @GetMapping("/admin")
    @Operation(summary = "admin controller")
    public String admin(Model model) {
        log.info("admin controller" + model);
        return "admin";
    }
    @GetMapping("/room")
    @Operation(summary = "room controller")
    public String room(@RequestParam Long room_id, Model model) {
        log.info("room controller" + model);
        log.info("room_id: " + room_id);

        TeamDTO teamDTO = teamService.getTeamById(room_id);

        model.addAttribute("team", teamDTO);
        return "/team/room";
    }
    @GetMapping("/team")
    public String team(Model model) {
        return "team";
    }
    @GetMapping("/team/create")
    public String createTeam(Model model) {
        log.info("create team" + model);
        return "team/create";
    }
}
