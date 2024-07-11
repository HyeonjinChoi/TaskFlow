package com.sparta.taskflow.domain.section.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.domain.section.dto.SectionRequestDto;
import com.sparta.taskflow.domain.section.dto.SectionResponseDto;
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
		return ResponseEntity.status(HttpStatus.CREATED).
			body(new CommonDto<SectionResponseDto>(HttpStatus.CREATED.value(), "섹션 생성에 성공하였습니다.", responseDto));
	}
}
