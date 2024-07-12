package com.sparta.taskflow.domain.card.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.domain.card.dto.CardRequestDto;
import com.sparta.taskflow.domain.card.dto.CardResponseDto;
import com.sparta.taskflow.domain.card.service.CardService;
import com.sparta.taskflow.domain.section.dto.BoardIdRequestDto;
import com.sparta.taskflow.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardController {

	private final CardService cardService;

	@PostMapping("/cards")
	public ResponseEntity<CommonDto<CardResponseDto>> createCard(
		@RequestBody CardRequestDto requestDto,
		User user) {

		CardResponseDto card = cardService.createCard(requestDto, user);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(new CommonDto<>(HttpStatus.CREATED.value(), "카드 생성에 성공하였습니다.", card));
	}

	@GetMapping("/cards")
	public ResponseEntity<CommonDto<Page<CardResponseDto>>> getCards(
		@RequestBody BoardIdRequestDto boardIdRequestDto,
		@RequestParam int page) {

		Page<CardResponseDto> cards = cardService.getCards(boardIdRequestDto, page);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new CommonDto<>(HttpStatus.OK.value(), "카드 조회에 성공하였습니다.", cards));

	}
}
