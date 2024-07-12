package com.sparta.taskflow.domain.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.taskflow.domain.card.entity.Card;
import com.sparta.taskflow.domain.section.entity.Section;

public interface CardRepository extends JpaRepository<Card, Long> {
	boolean existsByTitleAndSection(String title, Section section);
	int countBySection(Section section);
}
