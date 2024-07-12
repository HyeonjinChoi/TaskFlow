package com.sparta.taskflow.domain.card.dto;

import java.time.LocalDateTime;

import com.sparta.taskflow.domain.card.entity.Card;

import lombok.Getter;

@Getter
public class CardResponseDto {

	private final String title;
	private final String contents;
	private final String nickname;
	private final int position;
	private final LocalDateTime createdAt;
	private final LocalDateTime modifiedAt;

	public CardResponseDto(Card card) {
		this.title = card.getTitle();
		this.contents = card.getContents();
		this.nickname = card.getUser().getNickname();
		this.position = card.getPosition();
		this.createdAt = card.getCreatedAt();
		this.modifiedAt = card.getModifiedAt();
	}
}
