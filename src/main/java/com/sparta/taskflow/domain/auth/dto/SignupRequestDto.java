package com.sparta.taskflow.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SignupRequestDto {

    @NotNull(message = "아이디를 입력해주세요.")
    @Size(min=6, max=15, message = " 아이디는 최소 6자 이상, 15자 이하여야 합니다.")
    private String username;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @NotNull(message = "변경할 비밀번호를 입력해주세요.")
    @Size(min=8, max=15, message = "비밀번호는 최소 8자 이상, 15자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",message = "password 는 알파벳 대소문자, 숫자, 특수문자가 최소 한개 씩 포함되어야 합니다.")

    private String password;

    @NotNull(message = "이매일을 입력해주세요.")
    @Email
    private String email;

    @NotNull(message = "닉네임을 입력해주세요.")
    private String nickname;

    private String introduction;

    private String rolePassword;

}
