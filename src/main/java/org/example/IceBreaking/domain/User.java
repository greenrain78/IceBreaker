package org.example.IceBreaking.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private String userId;          // 로그인시 필요한 userId
    private String password;
    private String name;
    private String department;      // 학과
    private String studentId;       // 학번
    private String[] interests;     // 관심사 키워드 list

    public boolean matchPassword(String password) {
        if (password == null) {
            return false;
        }
        return this.password.equals(password);
    }
}
