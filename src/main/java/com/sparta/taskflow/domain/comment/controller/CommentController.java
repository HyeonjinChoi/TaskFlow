package com.sparta.taskflow.domain.comment.controller;

import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.domain.comment.dto.CommentRequestDto;
import com.sparta.taskflow.domain.comment.dto.CommentResponseDto;
import com.sparta.taskflow.domain.comment.service.CommentService;
import com.sparta.taskflow.security.principal.UserDetailsImpl;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetailis, ) {
        CommentResponseDto responseDto = commentService.createComment(requestDto, userDetailis.getUser(),cardId);
        CommonDto<CommentResponseDto> response = new CommonDto<>(HttpStatus.OK.value(), "댓글 생성에 성공하셨습니다", responseDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetailis, @PathVariable("cardId") Long cardId, @PathVariable("commentId") Long commentId) {
        CommentResponseDto responseDto = commentService.updateComment(requestDto, userDetailis.getUser(), cardId, commentId);
        CommonDto<CommentResponseDto> response = new CommonDto<>(HttpStatus.OK.value(), "댓글 수정에 성공하셨습니다", responseDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @DeleteMapping("comments/{commentId}")
    public ResponseEntity<?> deleteComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetailis, @PathVariable("cardId") Long cardId, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(requestDto, userDetailis.getUser(), cardId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("답글 삭제에 성공하셨습니다.");
    }

}

