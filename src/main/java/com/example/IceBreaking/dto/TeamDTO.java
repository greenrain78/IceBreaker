package com.example.IceBreaking.dto;

import com.example.IceBreaking.entity.TeamEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class TeamDTO {
    private Long id;
    private String teamName;
    private String teamType;
    private List<String> usernameList;
    private Map<String, String> settings;
    private LocalDateTime createdAt;

    public TeamEntity toEntity() {
        return TeamEntity.builder()
                .teamName(teamName)
                .teamType(teamType)
                .usernameList(usernameList)
                .settings(settings)
                .build();
    }
    public static TeamDTO of(TeamEntity teamEntity) {
        return TeamDTO.builder()
                .id(teamEntity.getId())
                .teamName(teamEntity.getTeamName())
                .teamType(teamEntity.getTeamType())
                .usernameList(teamEntity.getUsernameList())
                .settings(teamEntity.getSettings())
                .createdAt(teamEntity.getCreatedAt())
                .build();
    }

}
