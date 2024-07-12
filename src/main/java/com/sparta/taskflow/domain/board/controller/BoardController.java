package com.sparta.taskflow.domain.board.controller;

import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.domain.board.dto.*;
import com.sparta.taskflow.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/boards")
    public ResponseEntity<?> createBoard(@RequestBody BoardReqDto reqDto) {
        BoardResDto responseDto = boardService.createBoard(reqDto);
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                , "보드가 생성됩니다!"
                , responseDto));
    }

    @GetMapping("/boards")
    public ResponseEntity<?> getBoards() {
        List<BoardResDto> responseDto = boardService.getBoards();
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                , "보드 전체 조회"
                , responseDto));
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId) {
        BoardResDto responseDto = boardService.getBoard(boardId);
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                , "보드 단건 조회!"
                , responseDto));
    }

    @PutMapping("/boards/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId, @RequestBody BoardReqDto reqDto) {
        BoardResDto responseDto = boardService.updateBoard(boardId, reqDto);
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                , "보드가 수정됩니다!"
                , responseDto));
    }

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                , "보드가 삭제됩니다!"
                , null));
    }

//    @PostMapping(/boards/{boardId}/invitations)
//    public ResponseEntity<?> inviteUser(@PathVariable Long boardId, @RequestBody BoardInviteReqDto reqDto) {
//        boardService.inviteUser(boardId, reqDto);
//        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
//                , "회원을 초대합니다."
//                , null));
//    }
}
