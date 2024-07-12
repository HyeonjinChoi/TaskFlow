package com.sparta.taskflow.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateReqDto {

    private String nickname;

    private String instroduction;
}
