package com.sparta.taskflow.domain.board.repository;

import com.sparta.taskflow.domain.board.entity.Board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Boolean existsByIdAndUserId(Long userId, Long boardId);

}
