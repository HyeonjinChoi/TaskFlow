package com.sparta.taskflow.domain.board.controller;

import com.sparta.taskflow.common.aop.TokenUpdateRequired;
import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.domain.board.dto.*;
import com.sparta.taskflow.domain.board.repository.BoardInvitationRepository;
import com.sparta.taskflow.domain.board.service.BoardInvitationService;
import com.sparta.taskflow.domain.board.service.BoardService;
import com.sparta.taskflow.security.principal.UserDetailsImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;
    private final BoardInvitationRepository boardInvitationRepository;
    private final BoardInvitationService boardInvitationService;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @TokenUpdateRequired
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody BoardReqDto reqDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResDto responseDto = boardService.createBoard(reqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.CREATED.value()
                , "보드가 생성됩니다!"
                , responseDto));
    }

    @GetMapping
    public ResponseEntity<?> getBoards(
        @RequestParam int page) {

        Page<BoardResDto> responseDto = boardService.getBoards(page);
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
            , "보드 전체 조회"
            , responseDto));
    }


    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId) {
        BoardResDto responseDto = boardService.getBoard(boardId);
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                , "보드 단건 조회!"
                , responseDto));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @TokenUpdateRequired
    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId, @RequestBody BoardReqDto reqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResDto responseDto = boardService.updateBoard(boardId, reqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                , "보드가 수정됩니다!"
                , responseDto));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @TokenUpdateRequired
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(boardId, userDetails.getUser());
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                , "보드가 삭제됩니다!"
                , null));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @TokenUpdateRequired
    @PostMapping("{boardId}/invitations")
    public ResponseEntity<?> inviteMember(@PathVariable Long boardId, @RequestBody BoardInviteReqDto reqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardInvitationService.inviteUser(boardId, reqDto,userDetails.getUser());
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                , "회원 초대에 성공했습니다."
                , null));
    }
}
