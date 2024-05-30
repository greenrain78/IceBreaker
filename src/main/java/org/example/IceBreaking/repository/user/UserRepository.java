package org.example.IceBreaking.repository.user;

import org.example.IceBreaking.domain.Team;
import org.example.IceBreaking.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void save(User user);

    User findById(String userId);

    List<User> findAll();

    User changeUserInfo(User user);

    void saveEnteredChatRoom(String userId, Team team);

    List<Team> findEnteredChatRoom(String userId);

}
