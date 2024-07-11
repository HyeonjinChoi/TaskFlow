package com.sparta.taskflow.domain.board.repository;

import com.sparta.taskflow.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
