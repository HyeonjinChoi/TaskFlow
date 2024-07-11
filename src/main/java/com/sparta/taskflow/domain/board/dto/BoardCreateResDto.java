package com.sparta.taskflow.domain.board.dto;

import com.sparta.taskflow.domain.board.entity.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardCreateResDto {
    private Long id;
    private String name;
    private String description;

    public BoardCreateResDto(Board board) {
        this.id = board.getId();
        this.name = board.getName();
        this.description = board.getDescription();
    }
}
