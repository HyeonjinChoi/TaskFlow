package com.sparta.taskflow.domain.board.service;

import com.sparta.taskflow.common.exception.BusinessException;
import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.domain.board.dto.BoardReqDto;
import com.sparta.taskflow.domain.board.dto.BoardResDto;
import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.board.repository.BoardRepository;
import com.sparta.taskflow.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;


    @Transactional
        public BoardResDto createBoard(BoardReqDto reqDto, User user) {
        if (reqDto.getName() == null || reqDto.getDescription() == null) {
            throw new BusinessException(ErrorCode.FAIL_AUTHENTICATION);
        }

        Board board = new Board(reqDto,user);
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
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
        return new BoardResDto(board);
    }


    // 보드 수정
    @Transactional
    public BoardResDto updateBoard(Long boardId, BoardReqDto reqDto) {
        if (reqDto.getName() == null || reqDto.getDescription() == null) {
            throw new BusinessException(ErrorCode.BOARD_CREATE_MISSING_DATA);
        }
        findBoard(boardId);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
        board.update(reqDto);
        return new BoardResDto(board);
    }

    // 보드 삭제
    @Transactional
    public void deleteBoard(Long boardId) {
        findBoard(boardId);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
        boardRepository.delete(board);
    }
    public Board findBoard(Long id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
    }




}
