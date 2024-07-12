package com.sparta.taskflow.domain.section.controller;

import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.domain.section.dto.*;
import com.sparta.taskflow.domain.section.service.SectionService;
import com.sparta.taskflow.security.principal.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sections")
public class SectionController {

	private final SectionService sectionService;

	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping
	public ResponseEntity<CommonDto<SectionResponseDto>> createSection (
			@RequestBody SectionRequestDto requestDto,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		SectionResponseDto responseDto = sectionService.createSection(requestDto, userDetails.getUser());
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(new CommonDto<>(HttpStatus.CREATED.value(), "섹션 생성에 성공하였습니다.", responseDto));
	}

	@GetMapping
	public ResponseEntity<CommonDto<Page<SectionResponseDto>>> getSections (
		@RequestBody BoardIdRequestDto boardIdRequestDto,
		@RequestParam int page) {

		Page<SectionResponseDto> sections = sectionService.getSections(boardIdRequestDto, page);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new CommonDto<>(HttpStatus.OK.value(), "섹션 조회에 성공하였습니다.", sections));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping("/{sectionId}")
	public ResponseEntity<CommonDto<SectionResponseDto>> updateSection(
		@PathVariable Long sectionId,
		@RequestBody SectionUpdateRequestDto sectionUpdateRequestDto) {

		SectionResponseDto card = sectionService.updateSection(sectionId, sectionUpdateRequestDto);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new CommonDto<>(HttpStatus.OK.value(), "섹션 수정에 성공하였습니다.", card));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping("/{sectionId}")
	public ResponseEntity<CommonDto<Void>> deleteSection (
		@PathVariable Long sectionId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		sectionService.deleteSection(sectionId, userDetails.getUser());
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.body(new CommonDto<>(HttpStatus.NO_CONTENT.value(), "섹션 삭제에 성공하였습니다.", null));
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PutMapping("/move")
	public ResponseEntity<CommonDto<Void>> updateSectionPosition(
		@RequestBody UpdateSectionPositionDto updateSectionPositionDto) {

		sectionService.updateSectionPosition(updateSectionPositionDto);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new CommonDto<>(HttpStatus.OK.value(), "섹션 순서 변경에 성공하였습니다.", null));
	}
}
