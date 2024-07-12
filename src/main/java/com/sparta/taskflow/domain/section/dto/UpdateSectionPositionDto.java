package com.sparta.taskflow.domain.section.dto;

import lombok.Getter;

@Getter
public class UpdateSectionPositionDto {
	private Long userId;
	private Long sectionId;
	private int newPosition;
}
