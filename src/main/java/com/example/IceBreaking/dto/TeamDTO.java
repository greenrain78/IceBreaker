package com.example.IceBreaking.dto;

import com.example.IceBreaking.entity.TeamEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeamDTO {
    private String teamName;
    private List<String> usernameList;

    @Builder
    public TeamDTO(String teamName, List<String> usernameList) {
        this.teamName = teamName;
        this.usernameList = usernameList;
    }

    public TeamEntity toEntity() {
        return TeamEntity.builder()
                .teamName(teamName)
                .usernameList(usernameList)
                .build();
    }
    public static TeamDTO of(TeamEntity teamEntity) {
        return TeamDTO.builder()
                .teamName(teamEntity.getTeamName())
                .usernameList(teamEntity.getUsernameList())
                .build();
    }
}
