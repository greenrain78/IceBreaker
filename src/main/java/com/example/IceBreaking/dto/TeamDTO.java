package com.example.IceBreaking.dto;

import com.example.IceBreaking.entity.TeamEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TeamDTO {
    private String teamName;
    private List<String> usernameList;
    private LocalDateTime createdAt;

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
                .createdAt(teamEntity.getCreatedAt())
                .build();
    }

}
