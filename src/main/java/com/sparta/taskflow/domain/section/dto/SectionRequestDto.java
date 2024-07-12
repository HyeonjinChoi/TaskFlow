package com.sparta.taskflow.domain.section.dto;

import lombok.Getter;

@Getter
public class SectionRequestDto {
	private String contents;
	private Long userId;
	private Long boardId;
}
