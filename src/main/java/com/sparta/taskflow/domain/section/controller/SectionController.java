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
import com.sparta.taskflow.domain.section.dto.UpdateSectionPositionDto;
import com.sparta.taskflow.domain.section.service.SectionService;
import com.sparta.taskflow.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SectionController {

	private final SectionService sectionService;

	@PostMapping("/section")
	public ResponseEntity<CommonDto<SectionResponseDto>> createSection (
		@RequestBody SectionRequestDto requestDto,
		User user) {

		SectionResponseDto responseDto = sectionService.createSection(requestDto, user);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(new CommonDto<>(HttpStatus.CREATED.value(), "섹션 생성에 성공하였습니다.", responseDto));
	}

	@GetMapping("/section")
	public ResponseEntity<CommonDto<Page<SectionResponseDto>>> getSections (
		@RequestBody BoardIdRequestDto boardIdRequestDto,
		@RequestParam int page) {

		Page<SectionResponseDto> sections = sectionService.getSections(boardIdRequestDto, page);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new CommonDto<>(HttpStatus.OK.value(), "섹션 조회에 성공하였습니다.", sections));
	}

	@DeleteMapping("/section/{sectionId}")
	public ResponseEntity<CommonDto<Void>> deleteSection (
		@PathVariable Long sectionId,
		User user) {

		sectionService.deleteSection(sectionId, user);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
			.body(new CommonDto<>(HttpStatus.NO_CONTENT.value(), "섹션 삭제에 성공하였습니다.", null));
	}
}
