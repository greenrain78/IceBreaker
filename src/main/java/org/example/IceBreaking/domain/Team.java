package org.example.IceBreaking.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Team {

    private static int nextId = 1;

    private String teamName;
    private int id;             // team의 고유 id 값

    public Team(String teamName) {
        this.teamName = teamName;
        this.id = nextId++;
    }
}
