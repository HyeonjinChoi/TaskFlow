package com.sparta.taskflow.domain.board.service;

import com.sparta.taskflow.domain.board.dto.*;
import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.board.entity.BoardInvitation;
import com.sparta.taskflow.domain.board.repository.BoardInvitationRepository;
import com.sparta.taskflow.domain.board.repository.BoardRepository;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardInvitationRepository boardInvitationRepository;
    private final UserRepository userRepository;
    @Transactional
    public BoardResDto createBoard(BoardReqDto reqDto) {
        if (reqDto.getName() == null || reqDto.getDescription() == null) {
            throw new IllegalArgumentException("보드 이름과 설명은 필수입니다.");
        }

        Board board = new Board(reqDto);
        Board savedBoard = boardRepository.save(board);
        return new BoardResDto(savedBoard);
    }

    // 모든 보드 조회
    @Transactional
    public List<BoardResDto> getBoards() {
        return boardRepository.findAllByOrderByCreatedAtDesc().stream().map(BoardResDto::new).toList();
    }

    // 보드 단건 조회
    @Transactional
    public BoardResDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다."));
        return new BoardResDto(board);
    }


    // 보드 수정
    @Transactional
    public BoardResDto updateBoard(Long boardId, BoardReqDto reqDto) {
        if (reqDto.getName() == null || reqDto.getDescription() == null) {
            throw new IllegalArgumentException("보드 이름과 설명은 필수입니다.");
        }
        findBoard(boardId);
        Board board = boardRepository.findById(boardId).get();
        board.update(reqDto);
        return new BoardResDto(board);
    }

    // 보드 삭제
    @Transactional
    public void deleteBoard(Long boardId) {
        findBoard(boardId);
        Board board = boardRepository.findById(boardId).get();
        boardRepository.delete(board);
    }

    public void inviteUser(Long boardId, BoardInviteReqDto reqDto) {

        Board board = findBoard(boardId);

        if (boardInvitationRepository.existsByBoardIdAndUserId(boardId, reqDto.getUserId())){
            throw new IllegalArgumentException("이미 초대 되었습니다. ");
        }
        userRepository.findById(reqDto.getUserId())
                .ifPresent(user -> boardInvitationRepository.save(new BoardInvitation(user, board)));
    }

    public Board findBoard(Long id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다."));
    }

    public boolean canCreateCard(Long boardId, Long userId) {
        return boardInvitationRepository.existsByBoardIdAndUserId(boardId, userId);
    }
}
