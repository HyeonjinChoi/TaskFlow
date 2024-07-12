package com.sparta.taskflow.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateReqDto {
    @NotNull(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotNull(message = "변경할 비밀번호를 입력해주세요.")
    @Size(min=8, max=15, message = "password는 최소 8자 이상, 15자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",message = "password는 알파벳 대소문자, 숫자, 특수문자가 최소 한개 씩 포함되어야 합니다.")
    private String newPassword;
}
