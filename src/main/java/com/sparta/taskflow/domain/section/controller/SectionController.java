package com.sparta.taskflow.domain.section.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.domain.section.dto.BoardIdRequestDto;
import com.sparta.taskflow.domain.section.dto.SectionRequestDto;
import com.sparta.taskflow.domain.section.dto.SectionResponseDto;
import com.sparta.taskflow.domain.section.dto.SectionUpdateRequestDto;
import com.sparta.taskflow.domain.section.dto.UpdateSectionPositionDto;
import com.sparta.taskflow.domain.section.service.SectionService;
import com.sparta.taskflow.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SectionController {

	private final SectionService sectionService;

	@PostMapping("/sections")
	public ResponseEntity<CommonDto<SectionResponseDto>> createSection (
		@RequestBody SectionRequestDto requestDto,
		User user) {

		SectionResponseDto responseDto = sectionService.createSection(requestDto, user);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(new CommonDto<>(HttpStatus.CREATED.value(), "섹션 생성에 성공하였습니다.", responseDto));
	}

	@GetMapping("/sections")
	public ResponseEntity<CommonDto<Page<SectionResponseDto>>> getSections (
		@RequestBody BoardIdRequestDto boardIdRequestDto,
		@RequestParam int page) {

		Page<SectionResponseDto> sections = sectionService.getSections(boardIdRequestDto, page);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new CommonDto<>(HttpStatus.OK.value(), "섹션 조회에 성공하였습니다.", sections));
	}

	@PutMapping("/cards/{cardId}")
	public ResponseEntity<CommonDto<SectionResponseDto>> updateSection(
		@PathVariable Long cardId,
		@RequestBody SectionUpdateRequestDto sectionUpdateRequestDto) {

		SectionResponseDto card = sectionService.updateSection(cardId, sectionUpdateRequestDto);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new CommonDto<>(HttpStatus.OK.value(), "섹션 수정에 성공하였습니다.", card));
	}

	@DeleteMapping("/sections/{sectionId}")
	public ResponseEntity<CommonDto<Void>> deleteSection (
		@PathVariable Long sectionId,
		User user) {

		sectionService.deleteSection(sectionId, user);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.body(new CommonDto<>(HttpStatus.NO_CONTENT.value(), "섹션 삭제에 성공하였습니다.", null));
	}

	@PutMapping("/sections")
	public ResponseEntity<CommonDto<Void>> updateSectionPosition(
		@RequestBody UpdateSectionPositionDto updateSectionPositionDto) {

		sectionService.updateSectionPosition(updateSectionPositionDto);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new CommonDto<>(HttpStatus.OK.value(), "섹션 순서 변경에 성공하였습니다.", null));
	}
}
