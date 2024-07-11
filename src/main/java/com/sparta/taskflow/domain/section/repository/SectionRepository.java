package com.sparta.taskflow.domain.section.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.section.entity.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
	boolean existsByTitleAndBoard(String title, Board board);
	int countByBoard(Board board);
}
