package org.example.IceBreaking.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.IceBreaking.domain.Team;
import org.example.IceBreaking.domain.User;
import org.example.IceBreaking.repository.team.TeamRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamRepository teamRepository;
    private final HttpSession httpSession;

    @GetMapping("/team/showCreateForm")
    public String showCreateTeamForm() {
        return "/team/create";
    }

    @PostMapping("/team/create")
    public String createTeam(@RequestParam("teamName") String teamName) {
        Team team = new Team(teamName);
        teamRepository.save(team);

        // 팀플방 생성한 User의 id값 저장
        User teamCreator = (User) httpSession.getAttribute("loginedUser");
        teamRepository.saveTeamCreator(team.getId(), teamCreator.getUserId());

        return "redirect:/";            // 홈화면으로 redirect
    }

    @GetMapping("/team/showAll")
    public String showAllTeams(Model model) {
        List<Team> allTeams = teamRepository.findAll();
        model.addAttribute("allTeams", allTeams);

        return "/team/all";
    }
}
