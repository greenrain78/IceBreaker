package com.example.IceBreaking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> usernameList;

    @Column(nullable = false, unique = true)
    private String teamName;
    @Builder
    public TeamEntity(List<String> usernameList, String teamName) {
        this.usernameList = usernameList;
        this.teamName = teamName;
    }
}
