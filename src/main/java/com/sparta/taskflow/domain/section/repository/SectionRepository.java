package com.sparta.taskflow.domain.section.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.section.entity.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
	boolean existsByTitleAndBoard(String title, Board board);
	int countByBoard(Board board);

	Page<Section> findByBoard(Board board, Pageable pageable);

	List<Section> findByBoardOrderByPositionAsc(Board board);
}
