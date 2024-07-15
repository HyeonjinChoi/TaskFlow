package com.sparta.taskflow.domain.auth.dto;

import com.sparta.taskflow.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponsDto {
    private String username;
    private String password;
    private String nickname;
    private User.Role role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // 생성자
    public SignupResponsDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
