package com.sparta.taskflow.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardUpdateReqDto {
    private String name;
    private String description;
}