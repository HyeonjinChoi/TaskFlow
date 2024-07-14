package com.sparta.taskflow.common.dataloader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.board.entity.BoardInvitation;
import com.sparta.taskflow.domain.board.repository.BoardInvitationRepository;
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
	private final BoardInvitationRepository boardInvitationRepository;
	private final SectionRepository sectionRepository;
	private final CardRepository cardRepository;
	private final CommentRepository commentRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		int userCount = 400;
		List<String> usernames = DataGenerator.generateUniqueUsernames(userCount);
		List<String> emails = DataGenerator.generateUniqueEmails(userCount);
		List<String> nicknames = DataGenerator.generateUniqueNicknames(userCount);
		List<String> sectionContents = DataGenerator.generateUniqueSectionContents(userCount * 3);
		List<String> cardTitles = DataGenerator.generateUniqueCardTitles(userCount * 3 * 10);

		List<User> users = createUsers(userCount, usernames, emails, nicknames);
		List<Board> boards = createBoards(users);
		List<Section> sections = createSections(boards, sectionContents);
		List<Card> cards = createCards(sections, cardTitles);
		createComments(cards);
	}

	private List<User> createUsers(int count, List<String> usernames, List<String> emails, List<String> nicknames) {
		List<User> users = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < count; i++) {
			User.Role role = random.nextBoolean() ? User.Role.USER : User.Role.MANAGER;
			User user = User.builder()
				.username(usernames.get(i))
				.password(passwordEncoder.encode(DataGenerator.generatePassword()))
				.email(emails.get(i))
				.nickname(nicknames.get(i))
				.introduction(DataGenerator.generateIntroduction())
				.role(role)
				.status(User.Status.NORMAL)
				.build();
			users.add(userRepository.save(user));
		}
		return users;
	}

	private List<Board> createBoards(List<User> users) {
		List<Board> boards = new ArrayList<>();
		for (User user : users) {
			if (user.getRole() == User.Role.MANAGER) {
				Board board = Board.builder()
					.name(DataGenerator.generateBoardName())
					.description(DataGenerator.generateBoardDescription())
					.user(user)
					.build();
				boards.add(boardRepository.save(board));

				for (User invitee : users) {
					if (invitee.getRole() == User.Role.USER) {
						BoardInvitation invitation = BoardInvitation.builder()
							.user(invitee)
							.board(board)
							.build();
						boardInvitationRepository.save(invitation);
					}
				}
			}
		}
		return boards;
	}

	private List<Section> createSections(List<Board> boards, List<String> sectionContents) {
		List<Section> sections = new ArrayList<>();
		int contentPosition = 0;
		for (Board board : boards) {
			for (int i = 0; i < 3; i++) {
				Section section = Section.builder()
					.contents(sectionContents.get(contentPosition))
					.position(i)
					.user(board.getUser())
					.board(board)
					.build();
				sections.add(sectionRepository.save(section));
				contentPosition++;
			}
		}
		return sections;
	}

	private List<Card> createCards(List<Section> sections, List<String> cardTitles) {
		List<Card> cards = new ArrayList<>();
		int titlePosition = 0;
		for (Section section : sections) {
			List<BoardInvitation> invitations = boardInvitationRepository.findByBoard(section.getBoard());
			List<User> invitedUsers = invitations.stream().map(BoardInvitation::getUser).toList();
			for (int i = 0; i < 10; i++) {
				User cardUser = invitedUsers.get(new Random().nextInt(invitedUsers.size()));
				Card card = Card.builder()
					.title(cardTitles.get(titlePosition))
					.contents(DataGenerator.generateCardContents())
					.dueDate(DataGenerator.generateDueDate())
					.position(i)
					.user(cardUser)
					.board(section.getBoard())
					.section(section)
					.build();
				cards.add(cardRepository.save(card));
				titlePosition++;
			}
		}
		return cards;
	}

	private void createComments(List<Card> cards) {
		for (Card card : cards) {
			for (int i = 0; i < 5; i++) {
				Comment comment = Comment.builder()
					.contents(DataGenerator.generateCommentContents())
					.user(card.getUser())
					.card(card)
					.build();
				commentRepository.save(comment);
			}
		}
	}
}
