package com.example.IceBreaking.service;

import com.example.IceBreaking.dto.ChatDTO;
import com.example.IceBreaking.entity.UserEntity;
import com.example.IceBreaking.repository.ChatRepository;
import com.example.IceBreaking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    final private UserRepository userRepository;

    @Transactional
    public UserEntity getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public String getSettingValue(String username, String key) {
        return userRepository.findByUsername(username).getSettings().get(key);
    }
    @Transactional
    public void setSettingValue(String username, String key, String value) {
        log.info("setSettingValue: username={}, key={}, value={}", username, key, value);
        UserEntity user = userRepository.findByUsername(username);
        user.getSettings().put(key, value);
        userRepository.save(user);
    }
}
