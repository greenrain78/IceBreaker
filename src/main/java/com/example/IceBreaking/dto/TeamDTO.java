package com.example.IceBreaking.dto;

import com.example.IceBreaking.entity.TeamEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TeamDTO {
    private Long id;
    private String teamName;
    private String teamType;
    private List<String> usernameList;
    private LocalDateTime createdAt;

    public TeamEntity toEntity() {
        return TeamEntity.builder()
                .teamName(teamName)
                .teamType(teamType)
                .usernameList(usernameList)
                .build();
    }
    public static TeamDTO of(TeamEntity teamEntity) {
        return TeamDTO.builder()
                .id(teamEntity.getId())
                .teamName(teamEntity.getTeamName())
                .usernameList(teamEntity.getUsernameList())
                .createdAt(teamEntity.getCreatedAt())
                .build();
    }

}
