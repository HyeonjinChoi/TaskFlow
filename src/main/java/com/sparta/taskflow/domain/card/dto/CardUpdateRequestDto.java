package com.sparta.taskflow.domain.card.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CardUpdateRequestDto {
	private String title;
	private String contents;
	private LocalDateTime dueDate;
}