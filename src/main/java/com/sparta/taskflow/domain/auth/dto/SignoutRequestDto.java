package com.sparta.taskflow.domain.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignoutRequestDto {

    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;






}
