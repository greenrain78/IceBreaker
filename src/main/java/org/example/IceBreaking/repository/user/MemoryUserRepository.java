package org.example.IceBreaking.repository.user;

import org.example.IceBreaking.domain.Team;
import org.example.IceBreaking.domain.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryUserRepository implements UserRepository {

    private final Map<String, User> userMap = new HashMap<>();
    private final Map<String, List<Team>> enteredChatRoomMap = new HashMap<>();     // user가 입장한 적이 있는 chatRoom 정보 저장

    @Override
    public void save(User user) {
        userMap.put(user.getUserId(), user);
    }

    @Override
    public User findById(String userId) {
        return userMap.get(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User changeUserInfo(User user) {
        if (userMap.get(user.getUserId()) != null) {
            userMap.put(user.getUserId(), user);
            return userMap.get(user.getUserId());
        }

        return null;
    }

    @Override
    public void saveEnteredChatRoom(String userId, Team team) {
        // userId로 List<Team> get
        List<Team> enteredTeamList = enteredChatRoomMap.getOrDefault(userId, new ArrayList<>());

        // team이 이미 enteredTeamList에 존재하는지 확인
        boolean exists = enteredTeamList.stream().anyMatch(t -> t.equals(team));

        // 존재하지 않는다면 enteredTeamList에 team을 add후 enteredChatRoomMap update
        if (!exists) {
            enteredTeamList.add(team);
            enteredChatRoomMap.put(userId, enteredTeamList);
        }

        // 검증용
        for (Team savedTeam : enteredTeamList) {
            System.out.println("savedTeam.getTeamName() = " + savedTeam.getTeamName());
        }
    }

    @Override
    public List<Team> findEnteredChatRoom(String userId) {
        return enteredChatRoomMap.get(userId);
    }

}
