package com.sparta.taskflow.domain.section.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSectionPositionDto {
	private Long sectionId;
	private int newPosition;
}
