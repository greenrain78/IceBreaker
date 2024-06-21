package org.example.IceBreaking.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserInterestDto {
    // 해당 팀플방에 입장한 user의 관심사 정보를 서버로 전송하기 위한 dto

    private String userId;
    private int teamId;
    private String[] interests;
}
