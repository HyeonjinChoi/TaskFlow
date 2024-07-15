package com.sparta.taskflow.domain.board.service;

import com.sparta.taskflow.common.exception.BusinessException;
import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.domain.board.dto.BoardInviteReqDto;
import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.board.entity.BoardInvitation;
import com.sparta.taskflow.domain.board.repository.BoardInvitationRepository;
import com.sparta.taskflow.domain.board.repository.BoardRepository;
import com.sparta.taskflow.domain.card.entity.Card;
import com.sparta.taskflow.domain.card.repository.CardRepository;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardInvitationService {

    private final  BoardRepository boardRepository;
    private final BoardInvitationRepository boardInvitationRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final BoardService boardService;

    public void inviteUser(Long boardId, BoardInviteReqDto reqDto) {

        Board board = boardService.findBoard(boardId);

        User user = userRepository.findByNickname(reqDto.getUsername()).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );

        if (boardInvitationRepository.existsByBoardIdAndUserId(boardId, user.getId())){
            throw new BusinessException(ErrorCode.BOARD_INVITE_ALREADY_MEMBER);
        }

        BoardInvitation boardInvitation = BoardInvitation.builder()
                .board(board)
                .user(user)
                .build();
        boardInvitationRepository.save(boardInvitation);

    }

    public boolean cardAllowedByCard(Long cardId, Long userId) {
        Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new BusinessException(ErrorCode.CARD_NOT_FOUND)
        );
        Long boardId = card.getBoard().getId();
        return cardAllowedByBoard(boardId, userId);
    }

    public boolean cardAllowedByBoard(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BusinessException(ErrorCode.BOARD_NOT_FOUND)
        );
        return isInvitation(boardId, userId) || isManager(userId,boardId);
    }

    private boolean isManager(Long userId,Long boardId) {
        return boardRepository.existsByIdAndUserId(boardId, userId);
    }

    private boolean isInvitation(Long boardId, Long userId) {
        return boardInvitationRepository.existsByBoardIdAndUserId(boardId, userId);
    }

}
