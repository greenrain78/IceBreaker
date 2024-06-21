package org.example.IceBreaking.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ValidateUserIdDto {
    // 아이디 중복 확인을 하기 위한 dto

    private String userId;
}
