package com.sparta.taskflow.domain.board.repository;

import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.board.entity.BoardInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardInvitationRepository extends JpaRepository<BoardInvitation, Long> {

    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
}
