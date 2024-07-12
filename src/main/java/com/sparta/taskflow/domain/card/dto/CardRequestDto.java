package com.sparta.taskflow.domain.card.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CardRequestDto {
	private String title;
	private String contents;
	private LocalDateTime dueDate;
	private Long userId;
	private Long boardId;
	private Long sectionId;
}
