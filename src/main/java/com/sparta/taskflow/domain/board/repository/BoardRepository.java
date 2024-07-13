package com.sparta.taskflow.domain.board.repository;

import com.sparta.taskflow.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByCreatedAtDesc();

    Boolean existsByIdAndUserId(Long userId, Long boardId);

}
