package com.sparta.taskflow.domain.card.service;

import java.util.List;
import java.util.Objects;

import com.sparta.taskflow.common.exception.BusinessException;
import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.domain.user.repository.UserRepository;
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
	private final UserRepository userRepository;

	public CardResponseDto createCard(
		CardRequestDto requestDto, User user) {

		Board board = findBoard(requestDto.getBoardId());
		Section section = findSection(requestDto.getSectionId());

		if (cardRepository.existsByTitleAndSection(requestDto.getTitle(), section)) {
			throw new BusinessException(ErrorCode.CARD_ALREADY_EXISTS);
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
	public Page<CardResponseDto> findCard(Long boardId, Long sectionId, int page,String username) {

		Board board = findBoard(boardId);
		Section section = findSection(sectionId);
		Pageable pageable = PageRequest.of(page, PageSize.CARD.getSize());
		if(username != null) {
			User user = userRepository.findByUsername(username).orElseThrow(
					() -> new BusinessException(ErrorCode.USER_NOT_FOUND)
			);
			Page<Card> cards = cardRepository.findByBoardAndSectionAndUser(board,section,pageable,user);
			List<CardResponseDto> cardDtos = cards.stream()
					.map(CardResponseDto::new)
					.toList();

			return new PageImpl<>(cardDtos, pageable, cards.getTotalElements());

		}else{
			Page<Card> cards = cardRepository.findByBoardAndSection(board,section,pageable);
			List<CardResponseDto> cardDtos = cards.stream()
					.map(CardResponseDto::new)
					.toList();

			return new PageImpl<>(cardDtos, pageable, cards.getTotalElements());
		}

	}

	@Transactional(readOnly = true)
	public CardResponseDto getCard(Long cardId) {
		Card card = findCard(cardId);
		return new CardResponseDto(card);
	}

	@Transactional
	public CardResponseDto updateCard(
		Long cardId, CardUpdateRequestDto requestDto,User user) {

		Card card = findCard(cardId);
		exceptionCardUser(user, card);
		card.update(requestDto.getTitle(), requestDto.getContents(), requestDto.getDueDate());

		return new CardResponseDto(cardRepository.save(card));
	}

	@Transactional
	public void deleteCard(
		Long cardId,
		User user) {

		Card card = findCard(cardId);
		exceptionCardUser(user, card);
		cardRepository.delete(card);
	}

	@Transactional
	public void updateCardPosition(
		UpdateCardPositionDto updateCardPositionDto,
		User user) {

		Card card = findCard(updateCardPositionDto.getCardId());

		Section oldSection = card.getSection();
		Section newSection = findSection(updateCardPositionDto.getSectionId());

		if (!Objects.equals(card.getUser().getId(), user.getId())) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED_ACTION);
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



	//:::::::::::::::::// tool box //::::::::::::::::://

	private static void exceptionCardUser(User user, Card card) {
		if (!Objects.equals(card.getUser().getId(), user.getId())) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED_ACTION);
		}
	}

	private Board findBoard(Long boardId) {
		return boardRepository.findById(boardId).orElseThrow(() ->
			new BusinessException(ErrorCode.BOARD_NOT_FOUND)
		);
	}

	private Section findSection(Long sectionId) {
		return sectionRepository.findById(sectionId).orElseThrow(() ->
			new BusinessException(ErrorCode.SECTION_NOT_FOUND)
		);
	}

	private Card findCard(Long cardId) {
		Card card = cardRepository.findById(cardId)
				.orElseThrow(() -> new BusinessException(ErrorCode.CARD_NOT_FOUND));
		return card;
	}
}
