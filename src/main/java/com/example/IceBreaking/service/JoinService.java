package com.example.IceBreaking.service;

import com.example.IceBreaking.dto.JoinDTO;
import com.example.IceBreaking.entity.UserEntity;
import com.example.IceBreaking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public void joinProcess(JoinDTO joinDTO) {
        // 이미 존재하는 유저네임인지 확인
        if (userRepository.existsByUsername(joinDTO.getUsername())) {
            return;
        }
        UserEntity data = new UserEntity();
        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setRole("ROLE_USER");
        userRepository.save(data);
    }
}