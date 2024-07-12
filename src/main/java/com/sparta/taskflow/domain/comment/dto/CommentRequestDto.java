package com.sparta.taskflow.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String contents;
    private Long cardId;
}
