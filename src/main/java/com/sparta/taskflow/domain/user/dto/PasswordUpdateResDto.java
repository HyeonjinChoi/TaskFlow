package com.sparta.taskflow.domain.user.dto;

import com.sparta.taskflow.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PasswordUpdateResDto {
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PasswordUpdateResDto(User user) {
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
