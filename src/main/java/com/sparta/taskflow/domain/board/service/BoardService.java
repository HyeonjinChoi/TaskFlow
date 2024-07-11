package com.sparta.taskflow.domain.board.service;

import com.sparta.taskflow.domain.board.dto.*;
import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    public BoardCreateResDto createBoard(BoardCreateReqDto reqDto) {

        //Board board = new Board(reqDto);
        boardRepository.save(board);
        return new BoardCreateResDto(board.getId());
    }

    public List<BoardResDto> getBoards() {
        return null;
    }

    public BoardResDto getBoard(Long boardId) {
        return null;
    }

    public BoardResDto updateBoard(Long boardId, BoardUpdateReqDto reqDto) {
    }

    public void deleteBoard(Long boardId) {
        
    }

    public void inviteUser(Long boardId, BoardInviteReqDto reqDto) {
    }
}
