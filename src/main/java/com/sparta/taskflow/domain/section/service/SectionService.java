package com.sparta.taskflow.domain.section.service;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.board.repository.BoardRepository;
import com.sparta.taskflow.domain.section.dto.BoardIdRequestDto;
import com.sparta.taskflow.domain.section.dto.SectionRequestDto;
import com.sparta.taskflow.domain.section.dto.SectionResponseDto;
import com.sparta.taskflow.domain.section.dto.UpdateSectionPositionDto;
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
			throw new IllegalArgumentException("같은 섹션이 존재합니다.");
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

	@Transactional(readOnly = true)
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

	@Transactional
	public void updateSectionPosition(
		UpdateSectionPositionDto updateSectionPositionDto) {

		Section section = sectionRepository.findById(updateSectionPositionDto.getSectionId())
			.orElseThrow(() -> new IllegalArgumentException("섹션이 존재하지 않습니다."));

		if (!Objects.equals(section.getUser().getUserId(), updateSectionPositionDto.getUserId())) {
			throw new IllegalArgumentException("사용자 권한이 없습니다.");
		}

		int newPosition = updateSectionPositionDto.getNewPosition();
		int oldPosition = section.getPosition();
		Board board = section.getBoard();

		List<Section> sections = sectionRepository.findByBoardOrderByPositionAsc(board);

		sections.forEach(s -> {
			if (newPosition < oldPosition && s.getPosition() >= newPosition && s.getPosition() < oldPosition) {
				s.updatePosition(s.getPosition() + 1);
			} else if (newPosition > oldPosition && s.getPosition() <= newPosition && s.getPosition() > oldPosition) {
				s.updatePosition(s.getPosition() - 1);
			} else if (Objects.equals(s.getColumnId(), section.getColumnId())) {
				s.updatePosition(newPosition);
			}
		});

		sectionRepository.saveAll(sections);
	}

	@Transactional
	public void deleteSection(
		Long sectionId,
		User user) {

		Section section = sectionRepository.findById(sectionId)
			.orElseThrow(() -> new IllegalArgumentException("섹션이 존재하지 않습니다."));

		if (!Objects.equals(section.getUser().getUserId(), user.getUserId())) {
			throw new IllegalArgumentException("사용자 권한이 없습니다.");
		}

		sectionRepository.delete(section);
	}

	private Board findBoard(Long boardId) {
		return boardRepository.findById(boardId).orElseThrow(() ->
			new IllegalArgumentException("보드가 존재하지 않습니다.")
		);
	}
}
