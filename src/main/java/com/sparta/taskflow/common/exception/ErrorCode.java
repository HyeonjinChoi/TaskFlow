package com.sparta.taskflow.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Basic
//	BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),


	// user
//	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER NOT FOUND"),


	// comment
//	COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 게시글이 없거나 댓글이 없습니다."),


	// board
//	POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 게시글이 없거나 댓글이 없습니다."),
	;

	private final HttpStatus status;
	private final String message;
}