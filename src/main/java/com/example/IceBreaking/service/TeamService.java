package com.example.IceBreaking.service;

import com.example.IceBreaking.dto.TeamDTO;
import com.example.IceBreaking.entity.TeamEntity;
import com.example.IceBreaking.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamSupportService teamSupportService;

    @Transactional
    public TeamDTO createTeam(String teamName, String teamType, String username) {
        Map<String, String> settings = new HashMap<>();
        if (teamType.equals("basic")) {
            settings.put("gptLimit", "-1");
        } else if (teamType.equals("welcome")) {
            settings.put("gptLimit", "5");
        }
        TeamDTO teamDTO = TeamDTO.builder()
                .teamName(teamName)
                .teamType(teamType)
                .usernameList(Collections.singletonList(username))
                .settings(settings)
                .build();
        TeamEntity teamEntity = teamRepository.save(teamDTO.toEntity());
        if (teamType.equals("welcome")) {
            teamSupportService.initWelcomeTeam(teamEntity.getId());
        }
        return TeamDTO.of(teamEntity);
    }

    @Transactional
    public TeamDTO joinTeam(String teamName, String username) {
        TeamEntity teamEntity = teamRepository.findByTeamName(teamName);
        if (teamEntity == null) {
            throw new IllegalArgumentException("존재하지 않는 팀입니다.");
        }
        teamEntity.getUsernameList().add(username);
        teamRepository.save(teamEntity);

        return TeamDTO.of(teamEntity);
    }

    @Transactional
    public void leaveTeam(String teamName, String username) {
        TeamEntity teamEntity = teamRepository.findByTeamName(teamName);
        if (teamEntity == null) {
            throw new IllegalArgumentException("존재하지 않는 팀입니다.");
        }
        teamEntity.getUsernameList().remove(username);
        if (teamEntity.getUsernameList().isEmpty()) {
            teamRepository.delete(teamEntity);
        } else {
            teamRepository.save(teamEntity);
        }
    }

    @Transactional
    public List<TeamDTO> getTeamsByUsername(String username) {
        List<TeamEntity> teamEntityList = teamRepository.findByUsernameListContains(username);
        // list 2 dto list
        return teamEntityList.stream()
                .map(TeamDTO::of)
                .toList();
    }

    @Transactional
    public TeamDTO getTeamById(Long id) {
        TeamEntity teamEntity = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
        return TeamDTO.of(teamEntity);
    }

    @Transactional
    public String getSettingValue(Long id, String key) {
        TeamEntity teamEntity = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
        return teamEntity.getSettings().get(key);
    }

    @Transactional
    public void updateSettings(Long id, String key, String value) {
        TeamEntity teamEntity = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
        teamEntity.getSettings().put(key, value);
        teamRepository.save(teamEntity);
    }
}