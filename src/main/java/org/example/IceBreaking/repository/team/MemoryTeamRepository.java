package org.example.IceBreaking.repository.team;

import org.example.IceBreaking.domain.Team;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryTeamRepository implements TeamRepository {

    private final Map<Integer, Team> teamMap = new HashMap<>();
    private final Map<Integer, String> teamCreatorMap = new HashMap<>();

    @Override
    public void save(Team team) {
        teamMap.put(team.getId(), team);
    }

    @Override
    public void saveTeamCreator(int teamId, String userId) {
        teamCreatorMap.put(teamId, userId);
    }

    @Override
    public List<Team> findAll() {
        return new ArrayList<>(teamMap.values());
    }

    @Override
    public Optional<Team> findById(int teamId) {
        return Optional.ofNullable(teamMap.get(teamId));
    }

    @Override
    public String findTeamCreatorId(int teamId) {
        return teamCreatorMap.get(teamId);
    }
}
