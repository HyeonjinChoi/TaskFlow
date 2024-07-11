package com.sparta.taskflow.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.taskflow.domain.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
