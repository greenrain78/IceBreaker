package com.example.IceBreaking.service;

import com.example.IceBreaking.dto.TeamDTO;
import com.example.IceBreaking.repository.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // 'test' 프로파일이 설정되어 있다고 가정
public class TeamServiceTest {
    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @AfterEach
    public void tearDown() {
        teamRepository.deleteAll();
    }

    @Test
    @DisplayName("팀 생성 테스트")
    public void createTeamTest() {
        // given
        String teamName = "팀 이름";
        String username = "사용자 이름";

        // when
        TeamDTO teamDTO = teamService.createTeam(teamName, username);

        // then
        assert teamDTO != null;
        assert teamDTO.getTeamName().equals(teamName);
        assert teamDTO.getUsernameList().size() == 1;
        assert teamDTO.getUsernameList().getFirst().equals(username);
    }

    @Test
    @DisplayName("팀 가입 테스트")
    public void joinTeamTest() {
        // given
        String teamName = "팀 이름";
        String username = "사용자 이름";
        TeamDTO teamDTO = teamService.createTeam(teamName, username);

        // when
        String newUsername = "새로운 사용자 이름";
        TeamDTO joinedTeamDTO = teamService.joinTeam(teamName, newUsername);

        // then
        assert joinedTeamDTO != null;
        assert joinedTeamDTO.getTeamName().equals(teamName);
        assert joinedTeamDTO.getUsernameList().size() == 2;
        assert joinedTeamDTO.getUsernameList().getFirst().equals(username);
        assert joinedTeamDTO.getUsernameList().getLast().equals(newUsername);

    }
}
