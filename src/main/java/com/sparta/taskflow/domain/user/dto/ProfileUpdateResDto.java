package com.sparta.taskflow.domain.user.dto;

import com.sparta.taskflow.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class ProfileUpdateResDto {
    private String nickname;
    private String introduction;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ProfileUpdateResDto(User user) {
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
