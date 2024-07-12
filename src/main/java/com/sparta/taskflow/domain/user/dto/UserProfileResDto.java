package com.sparta.taskflow.domain.user.dto;

import com.sparta.taskflow.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class UserProfileResDto {
    private Long id;
    private String nickname;
    private String username;
    private String email;
    private String introduction;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public UserProfileResDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.introduction = user.getIntroduction();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
