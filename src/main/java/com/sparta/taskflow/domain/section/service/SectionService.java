package com.sparta.taskflow.domain.section.service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.board.repository.BoardRepository;
import com.sparta.taskflow.domain.section.dto.BoardIdRequestDto;
import com.sparta.taskflow.domain.section.dto.SectionRequestDto;
import com.sparta.taskflow.domain.section.dto.SectionResponseDto;
import com.sparta.taskflow.domain.section.entity.Section;
import com.sparta.taskflow.domain.section.repository.SectionRepository;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final SectionRepository sectionRepository;

	private final int

	public SectionResponseDto createSection(
		SectionRequestDto requestDto,
		User user) {

		Board board = findBoard(requestDto.getBoardId());

		if (sectionRepository.existsByTitleAndBoard(requestDto.getTitle(), board)) {
			throw new IllegalArgumentException("같은 컬럼이 존재합니다.");
		}

		int position = sectionRepository.countByBoard(board);

		Section section = Section.builder()
			.title(requestDto.getTitle())
			.contents(requestDto.getContents())
			.position(position)
			.user(user)
			.board(board)
			.build();

		return new SectionResponseDto(sectionRepository.save(section));
	}

	public Page<SectionResponseDto> getSections(
		BoardIdRequestDto boardIdRequestDto,
		int page) {

		Board board = findBoard(boardIdRequestDto.getBoardId());
		Pageable pageable = PageRequest.of(page, 4);

		Page<Section> sections = sectionRepository.findByBoard(board, pageable);
		List<SectionResponseDto> sectionDtos = sections.stream()
			.map(SectionResponseDto::new)
			.toList();

		return new PageImpl<>(sectionDtos, pageable, sections.getTotalElements());
	}

	private Board findBoard(Long boardId) {
		return boardRepository.findById(boardId).orElseThrow(() ->
			new IllegalArgumentException("보드가 존재하지 않습니다.")
		);
	}
}
