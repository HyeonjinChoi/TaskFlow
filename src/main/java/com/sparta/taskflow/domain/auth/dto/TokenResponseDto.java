package com.sparta.taskflow.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResponseDto {
    private String accessToken;
}
