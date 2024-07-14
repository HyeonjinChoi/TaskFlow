package com.sparta.taskflow.domain.board.service;

import com.sparta.taskflow.common.exception.BusinessException;
import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.size.PageSize;
import com.sparta.taskflow.domain.board.dto.*;
import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.board.entity.BoardInvitation;
import com.sparta.taskflow.domain.board.repository.BoardInvitationRepository;
import com.sparta.taskflow.domain.board.repository.BoardRepository;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import com.sparta.taskflow.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardInvitationRepository boardInvitationRepository;
    private final UserRepository userRepository;

    @Transactional
    public BoardResDto createBoard(BoardReqDto reqDto, User user) {

        if (reqDto.getName() == null || reqDto.getDescription() == null) {
            throw new BusinessException(ErrorCode.FAIL_AUTHENTICATION);
        }

        Board board = Board.builder()
                .name(reqDto.getName())
                .description(reqDto.getDescription())
                .user(user)
                .build();
        Board savedBoard = boardRepository.save(board);
        return new BoardResDto(savedBoard);
    }

    // 모든 보드 조회
    @Transactional
    public Page<BoardResDto> getBoards(int page) {
        Pageable pageable = PageRequest.of(page, PageSize.BOARD.getSize());

        Page<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<BoardResDto> boardResDtos = boards.stream()
            .map(BoardResDto::new)
            .toList();
        return new PageImpl<>(boardResDtos, pageable, boards.getTotalElements());
    }

    // 보드 단건 조회
    @Transactional
    public BoardResDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
        return new BoardResDto(board);
    }


    // 보드 수정
    @Transactional
    public BoardResDto updateBoard(Long boardId, BoardReqDto reqDto, User user) {
        if(!isBoardByUser(boardId, user.getId())) {
            throw new BusinessException(ErrorCode.BOARD_NO_PERMISSION);
        }

        if (reqDto.getName() == null || reqDto.getDescription() == null) {
            throw new BusinessException(ErrorCode.BOARD_CREATE_MISSING_DATA);
        }
        Board board = findBoard(boardId);
        board.update(reqDto);
        return new BoardResDto(board);
    }

    // 보드 삭제
    @Transactional
    public void deleteBoard(Long boardId, User user) {
        if(!isBoardByUser(boardId, user.getId())) {
            throw new BusinessException(ErrorCode.BOARD_NO_PERMISSION);
        }
        Board board = findBoard(boardId);
        boardRepository.delete(board);
    }

    public Boolean isBoardByUser(Long boardId, Long userId) {
        return boardRepository.existsByIdAndUserId(boardId, userId);
    }

    public Board findBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
    }
}


