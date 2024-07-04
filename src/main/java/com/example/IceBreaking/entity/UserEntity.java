package com.example.IceBreaking.entity;

import com.example.IceBreaking.utils.HashMapConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@Setter
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;

    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, String> settings;

    @PrePersist
    protected void prePersist() {
        this.settings = new HashMap<>();
    }
    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}