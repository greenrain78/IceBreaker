package org.example.IceBreaking.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserInterestDto {

    private String userId;
    private int teamId;
    private String[] interests;
}
