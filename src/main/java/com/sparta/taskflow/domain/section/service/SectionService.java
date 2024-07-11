package com.sparta.taskflow.domain.section.service;

import java.util.Base64;

import org.springframework.stereotype.Service;

import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.board.repository.BoardRepository;
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

	private Board findBoard(Long boardId) {
		return boardRepository.findById(boardId).orElseThrow(() ->
			new IllegalArgumentException("보드가 존재하지 않습니다.")
		);
	}
}
