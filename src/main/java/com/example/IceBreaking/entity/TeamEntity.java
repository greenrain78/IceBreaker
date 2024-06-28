package com.example.IceBreaking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> usernameList;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public TeamEntity(List<String> usernameList, String teamName) {
        this.usernameList = usernameList;
        this.teamName = teamName;
    }
    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
