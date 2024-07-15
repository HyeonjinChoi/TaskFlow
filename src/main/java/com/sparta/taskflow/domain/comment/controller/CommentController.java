package com.sparta.taskflow.domain.comment.controller;

import com.sparta.taskflow.common.aop.TokenUpdateRequired;
import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.common.size.PageSize;
import com.sparta.taskflow.domain.comment.dto.CommentDeleteReqestDto;
import com.sparta.taskflow.domain.comment.dto.CommentRequestDto;
import com.sparta.taskflow.domain.comment.dto.CommentResponseDto;
import com.sparta.taskflow.domain.comment.service.CommentService;
import com.sparta.taskflow.security.principal.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @TokenUpdateRequired
    @PostMapping("")
    public ResponseEntity<?> createComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetailis ) {
        CommentResponseDto responseDto = commentService.createComment(requestDto, userDetailis.getUser());
        CommonDto<CommentResponseDto> response = new CommonDto<>(HttpStatus.OK.value(), "댓글 생성에 성공하셨습니다", responseDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @TokenUpdateRequired
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetailis, @PathVariable("commentId") Long commentId) {
        CommentResponseDto responseDto = commentService.updateComment(requestDto, userDetailis.getUser(), commentId);
        CommonDto<CommentResponseDto> response = new CommonDto<>(HttpStatus.OK.value(), "댓글 수정에 성공하셨습니다", responseDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @TokenUpdateRequired
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@RequestBody CommentDeleteReqestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetailis,  @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(requestDto, userDetailis.getUser(), commentId);
        return ResponseEntity.status(HttpStatus.OK).body("답글 삭제에 성공하셨습니다.");
    }

    @TokenUpdateRequired
    @GetMapping()
    public ResponseEntity<CommonDto<List<CommentResponseDto>>> getComments(
            @RequestParam Long cardId,
            @RequestParam(defaultValue = "0") int page // 기본값 0으로 설정
    ) {
        List<CommentResponseDto> responseDto = commentService.getComments(cardId, page, PageSize.COMMENT.getSize());

        CommonDto<List<CommentResponseDto>> response = new CommonDto<>(HttpStatus.OK.value(), "카드 조회에 성공하셨습니다.", responseDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}

