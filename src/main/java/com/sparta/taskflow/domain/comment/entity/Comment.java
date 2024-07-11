package com.sparta.taskflow.domain.comment.entity;

import com.sparta.taskflow.common.util.Timestamped;
import com.sparta.taskflow.domain.card.entity.Card;
import com.sparta.taskflow.domain.comment.dto.CommentRequestDto;
import com.sparta.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.config.Task;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "contents")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public Comment(CommentRequestDto requestDto, User user, Card card) {
        this.contents = requestDto.getContents();
        this.user = user;
        this.card = card;
    }

    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }

}
