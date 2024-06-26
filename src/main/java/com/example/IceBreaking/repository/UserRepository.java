package com.example.IceBreaking.repository;

import com.example.IceBreaking.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}