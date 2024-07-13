package com.sparta.taskflow.domain.board.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BoardInviteReqDto {
    @NotNull(message = "초대할 아이디를 입력해주세요.")
    private String username;
}
