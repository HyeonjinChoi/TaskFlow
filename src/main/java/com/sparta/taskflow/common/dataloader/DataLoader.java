package com.sparta.taskflow.common.dataloader;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.board.repository.BoardRepository;
import com.sparta.taskflow.domain.card.entity.Card;
import com.sparta.taskflow.domain.card.repository.CardRepository;
import com.sparta.taskflow.domain.comment.entity.Comment;
import com.sparta.taskflow.domain.comment.repository.CommentRepository;
import com.sparta.taskflow.domain.section.entity.Section;
import com.sparta.taskflow.domain.section.repository.SectionRepository;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final SectionRepository sectionRepository;
	private final CardRepository cardRepository;
	private final CommentRepository commentRepository;

	@Override
	public void run(String... args) throws Exception {
		List<User> users = createUsers(20);
		List<Board> boards = createBoards(users);
		List<Section> sections = createSections(boards);
		List<Card> cards = createCards(sections);
		createComments(cards);
	}

	private List<User> createUsers(int count) {
		List<User> users = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			User user = User.builder()
				.username(FakerExample.generateUsername())
				.password(FakerExample.generatePassword())
				.email(FakerExample.generateEmail())
				.nickname(FakerExample.generateNickname())
				.introduction(FakerExample.generateIntroduction())
				.role(User.Role.USER)
				.status(User.Status.NORMAL)
				.build();
			users.add(userRepository.save(user));
		}
		return users;
	}

	private List<Board> createBoards(List<User> users) {
		List<Board> boards = new ArrayList<>();
		for (User user : users) {
			Board board = Board.builder()
				.name(FakerExample.generateBoardName())
				.description(FakerExample.generateBoardDescription())
				.user(user)
				.build();
			boards.add(boardRepository.save(board));
		}
		return boards;
	}

	private List<Section> createSections(List<Board> boards) {
		List<Section> sections = new ArrayList<>();
		int position = 0;
		for (Board board : boards) {
			for (int i = 0; i < 3; i++) {
				Section section = Section.builder()
					.contents(FakerExample.generateSectionContents())
					.position(position++)
					.user(board.getUser())
					.board(board)
					.build();
				sections.add(sectionRepository.save(section));
			}
		}
		return sections;
	}

	private List<Card> createCards(List<Section> sections) {
		List<Card> cards = new ArrayList<>();
		int position = 0;
		for (Section section : sections) {
			for (int i = 0; i < 10; i++) {
				Card card = Card.builder()
					.title(FakerExample.generateCardTitle())
					.contents(FakerExample.generateCardContents())
					.dueDate(FakerExample.generateDueDate())
					.position(position++)
					.user(section.getUser())
					.board(section.getBoard())
					.section(section)
					.build();
				cards.add(cardRepository.save(card));
			}
		}
		return cards;
	}

	private void createComments(List<Card> cards) {
		for (Card card : cards) {
			for (int i = 0; i < 5; i++) {
				Comment comment = Comment.builder()
					.contents(FakerExample.generateCommentContents())
					.user(card.getUser())
					.card(card)
					.build();
				commentRepository.save(comment);
			}
		}
	}
}
