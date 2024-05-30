package org.example.IceBreaking.repository.team;

import org.example.IceBreaking.domain.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {

    void save(Team team);

    void saveTeamCreator(int teamId, String userId);

    List<Team> findAll();

    Optional<Team> findById(int teamId);

    String findTeamCreatorId(int teamId);
}
