package com.sparta.taskflow.domain.comment.service;

import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.common.exception.BusinessException;
import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.domain.card.entity.Card;
import com.sparta.taskflow.domain.card.repository.CardRepository;
import com.sparta.taskflow.domain.comment.dto.CommentDeleteReqestDto;
import com.sparta.taskflow.domain.comment.dto.CommentRequestDto;
import com.sparta.taskflow.domain.comment.dto.CommentResponseDto;
import com.sparta.taskflow.domain.comment.entity.Comment;
import com.sparta.taskflow.domain.comment.repository.CommentRepository;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    private final UserRepository userRepository;
    public CommentService(CommentRepository commentRepository, CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Card card = getCard(requestDto.getCardId());
        Comment comment = Comment.builder()
            .contents(requestDto.getContents())
            .card(card)
            .user(user)
            .build();
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public CommentResponseDto updateComment(CommentRequestDto requestDto, User user,  Long commentId) {
        if(!cardRepository.existsById(requestDto.getCardId())) throw new BusinessException(ErrorCode.CARD_NOT_FOUND);

        Comment comment = getCommet(commentId);

        comment.update(requestDto);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }


    public CommentResponseDto deleteComment(CommentDeleteReqestDto requestDto, User user, Long commentId) {
        if(!cardRepository.existsById(requestDto.getCardId())) throw new BusinessException(ErrorCode.CARD_NOT_FOUND);

        Comment comment = getCommet(commentId);

        commentRepository.delete(comment);
        return new CommentResponseDto(comment);
    }

    public Card getCard(Long cardId){
        return cardRepository.findById(cardId).orElseThrow(
                () -> new BusinessException(ErrorCode.CARD_NOT_FOUND)
        );
    }

    private Comment getCommet(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new BusinessException(ErrorCode.CARD_NOT_FOUND)
        );
    }



    public List<CommentResponseDto> getComments(CommentDeleteReqestDto requestDto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        Card card = getCard(requestDto.getCardId());
        Page<Comment> commentPage = commentRepository.findByCard(card, pageable);
        if (commentPage == null) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }
        commentPage = commentRepository.findByCard(card, pageable);


        return  commentPage.getContent().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());

    }
}
