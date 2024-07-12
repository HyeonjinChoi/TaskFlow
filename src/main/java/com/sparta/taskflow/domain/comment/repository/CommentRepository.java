package com.sparta.taskflow.domain.comment.repository;

import com.sparta.taskflow.domain.card.entity.Card;
import com.sparta.taskflow.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByCard(Card card, Pageable pageable);
}
