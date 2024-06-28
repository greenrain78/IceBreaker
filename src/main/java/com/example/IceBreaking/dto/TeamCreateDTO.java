package com.example.IceBreaking.dto;

import com.example.IceBreaking.entity.TeamEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreateDTO {
    private String teamName;
    private String teamType;
}
