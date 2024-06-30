package com.example.IceBreaking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long teamId;

    @Column(nullable = false)
    private String userName;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime time;

    @Builder
    public ChatEntity(Long teamId, String userName, String message) {
        this.teamId = teamId;
        this.userName = userName;
        this.message = message;
    }

    @PrePersist
    public void prePersist() {
        this.time = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ChatEntity{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
