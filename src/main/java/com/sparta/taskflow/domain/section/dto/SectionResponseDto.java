package com.sparta.taskflow.domain.section.dto;

import java.time.LocalDateTime;

import com.sparta.taskflow.domain.section.entity.Section;

import lombok.Getter;

@Getter
public class SectionResponseDto {

	private final Long id;
	private final String contents;
	private final String nickname;
	private final int position;
	private final LocalDateTime createdAt;
	private final LocalDateTime modifiedAt;

	public SectionResponseDto(Section section) {
		this.id = section.getSectionId();
		this.contents = section.getContents();
		this.nickname = section.getUser().getNickname();
		this.position = section.getPosition();
		this.createdAt = section.getCreatedAt();
		this.modifiedAt = section.getModifiedAt();
	}
}
