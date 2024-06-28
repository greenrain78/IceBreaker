package com.example.IceBreaking.repository;

import com.example.IceBreaking.entity.ChatEntity;
import com.example.IceBreaking.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    List<ChatEntity> findByTeamId(Long teamId);

    // findByTeamId limit 30
    List<ChatEntity> findTop30ByTeamIdOrderByTimeDesc(Long teamId);
}