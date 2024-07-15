package com.sparta.taskflow.domain.card.dto;

import lombok.Getter;

@Getter
public class UpdateCardPositionDto {
	private Long sectionId;
	private Long cardId;
	private int newPosition;
}
