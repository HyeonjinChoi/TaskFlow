package com.sparta.taskflow.domain.comment.repository;

import com.sparta.taskflow.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
