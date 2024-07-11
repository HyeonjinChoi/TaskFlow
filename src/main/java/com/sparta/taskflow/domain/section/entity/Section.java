package com.sparta.taskflow.domain.section.entity;

import java.util.ArrayList;
import java.util.List;

import com.sparta.taskflow.domain.board.entity.Board;
import com.sparta.taskflow.domain.comment.entity.Comment;
import com.sparta.taskflow.common.util.Timestamped;
import com.sparta.taskflow.domain.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "column")
public class Section extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "column_id")
	private Long columnId;

	@Column(name = "title")
	private String title;

	@Column(name = "contents")
	private String contents;

	@Column(name = "position", nullable = false)
	private int position;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	@OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	@Builder
	public Section(String title, String contents, int position, User user, Board board, List<Comment> comments) {
		this.title = title;
		this.contents = contents;
		this.position = position;
		this.user = user;
		this.board = board;
		this.comments = comments != null ? comments : new ArrayList<>();
	}
}
