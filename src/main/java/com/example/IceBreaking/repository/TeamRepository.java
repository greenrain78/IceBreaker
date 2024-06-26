package com.example.IceBreaking.repository;

import com.example.IceBreaking.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    List<TeamEntity> findByUsernameListContains(String username);

    TeamEntity findByTeamName(String teamName);
}