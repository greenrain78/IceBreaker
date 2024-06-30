package com.example.IceBreaking.entity;

import com.example.IceBreaking.utils.HashMapConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private String teamType;

    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> usernameList;

    @Lob
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, String> settings;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public TeamEntity(List<String> usernameList, String teamName, String teamType, Map<String, String> settings) {
        this.usernameList = usernameList;
        this.teamName = teamName;
        this.teamType = teamType;
        this.settings = settings;
    }
    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "TeamEntity{" +
                "id=" + id +
                ", teamName='" + teamName + '\'' +
                ", teamType='" + teamType + '\'' +
                ", usernameList=" + usernameList +
                ", settings=" + settings +
                ", createdAt=" + createdAt +
                '}';
    }
}
