package com.sparta.taskflow.domain.card.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.taskflow.common.size.PageSize;
import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.board.repository.BoardRepository;
import com.sparta.taskflow.domain.card.dto.CardRequestDto;
import com.sparta.taskflow.domain.card.dto.CardResponseDto;
import com.sparta.taskflow.domain.card.dto.CardUpdateRequestDto;
import com.sparta.taskflow.domain.card.dto.UpdateCardPositionDto;
import com.sparta.taskflow.domain.card.entity.Card;
import com.sparta.taskflow.domain.card.repository.CardRepository;
import com.sparta.taskflow.domain.section.dto.BoardIdRequestDto;
import com.sparta.taskflow.domain.section.entity.Section;
import com.sparta.taskflow.domain.section.repository.SectionRepository;
import com.sparta.taskflow.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {

	private final BoardRepository boardRepository;
	private final SectionRepository sectionRepository;
	private final CardRepository cardRepository;

	public CardResponseDto createCard(
		CardRequestDto requestDto,
		User user) {

		Board board = findBoard(requestDto.getBoardId());
		Section section = findSection(requestDto.getSectionId());

		if (cardRepository.existsByTitleAndSection(requestDto.getTitle(), section)) {
			throw new IllegalArgumentException("같은 카드가 존재합니다.");
		}

		int position = cardRepository.countBySection(section);

		Card card = Card.builder()
			.title(requestDto.getTitle())
			.contents(requestDto.getContents())
			.dueDate(requestDto.getDueDate())
			.position(position)
			.user(user)
			.board(board)
			.section(section)
			.build();

		return new CardResponseDto(cardRepository.save(card));
	}

	@Transactional(readOnly = true)
	public Page<CardResponseDto> getCards(
		BoardIdRequestDto boardIdRequestDto,
		int page) {

		Board board = findBoard(boardIdRequestDto.getBoardId());
		Pageable pageable = PageRequest.of(page, PageSize.CARD.getSize());

		Page<Card> cards = cardRepository.findByBoard(board, pageable);
		List<CardResponseDto> cardDtos = cards.stream()
			.map(CardResponseDto::new)
			.toList();

		return new PageImpl<>(cardDtos, pageable, cards.getTotalElements());
	}

	@Transactional(readOnly = true)
	public CardResponseDto getCard(Long cardId) {
		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new IllegalArgumentException("카드가 존재하지 않습니다."));

		return new CardResponseDto(card);
	}

	public CardResponseDto updateCard(
		Long cardId,
		CardUpdateRequestDto requestDto) {

		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new IllegalArgumentException("카드가 존재하지 않습니다."));

		card.update(requestDto.getTitle(), requestDto.getContents(), requestDto.getDueDate());

		return new CardResponseDto(cardRepository.save(card));
	}

	@Transactional
	public void deleteCard(
		Long cardId,
		User user) {

		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new IllegalArgumentException("카드가 존재하지 않습니다."));

		if (!Objects.equals(card.getUser().getUserId(), user.getUserId())) {
			throw new IllegalArgumentException("사용자 권한이 없습니다.");
		}

		cardRepository.delete(card);
	}

	@Transactional
	public void updateCardPosition(
		UpdateCardPositionDto updateCardPositionDto) {

		Card card = cardRepository.findById(updateCardPositionDto.getCardId())
			.orElseThrow(() -> new IllegalArgumentException("카드가 존재하지 않습니다."));
		Section oldSection = card.getSection();
		Section newSection = sectionRepository.findById(updateCardPositionDto.getSectionId())
			.orElseThrow(() -> new IllegalArgumentException("섹션이 존재하지 않습니다."));

		if (!Objects.equals(card.getUser().getUserId(), updateCardPositionDto.getUserId())) {
			throw new IllegalArgumentException("사용자 권한이 없습니다.");
		}

		List<Card> oldSectionCards = oldSection.getCards();
		List<Card> newSectionCards = newSection.getCards();
		int newPosition = updateCardPositionDto.getNewPosition();

		if (Objects.equals(oldSection.getSectionId(), newSection.getSectionId())) {
			oldSectionCards.forEach(c -> {
				if (c.getPosition() >= newPosition && c.getPosition() < card.getPosition()) {
					c.updatePosition(c.getPosition() + 1);
				} else if (c.getPosition() <= newPosition && c.getPosition() > card.getPosition()) {
					c.updatePosition(c.getPosition() - 1);
				}
			});
			card.updatePosition(newPosition);
		} else {
			oldSectionCards.remove(card);
			oldSectionCards.forEach(c -> {
				if (c.getPosition() > card.getPosition()) {
					c.updatePosition(c.getPosition() - 1);
				}
			});

			newSectionCards.add(card);
			newSectionCards.forEach(c -> {
				if (c.getPosition() >= newPosition) {
					c.updatePosition(c.getPosition() + 1);
				}
			});
			card.updateSection(newSection, newPosition);
		}

		cardRepository.saveAll(oldSectionCards);
		cardRepository.saveAll(newSectionCards);
	}

	private Board findBoard(Long boardId) {
		return boardRepository.findById(boardId).orElseThrow(() ->
			new IllegalArgumentException("보드가 존재하지 않습니다.")
		);
	}

	private Section findSection(Long sectionId) {
		return sectionRepository.findById(sectionId).orElseThrow(() ->
			new IllegalArgumentException("섹션이 존재하지 않습니다.")
		);
	}
}
