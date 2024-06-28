package com.example.IceBreaking.controller.page;


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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "room", description = "room controller")
public class RoomPageController {
    private final TeamService teamService;
    @GetMapping("/room")
    @Operation(summary = "room controller")
    public String room(@RequestParam Long teamId, Model model) {
        log.info("room controller" + model);
        log.info("room_id: " + teamId);

        TeamDTO teamDTO = teamService.getTeamById(teamId);
        model.addAttribute("team", teamDTO);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("name", username);
        return "/team/room";
    }
    @GetMapping("/new")
    @Operation(summary = "new room controller")
    public String newRoom(Model model) {
        log.info("new room controller" + model);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TeamDTO teamDTO = teamService.createTeam("임시채팅", username);
        return "redirect:/room?teamId=" + teamDTO.getId();
    }
}
