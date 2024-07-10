package com.sparta.taskflow.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Basic
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN"),
	NOT_FOUND(HttpStatus.NOT_FOUND, "NOT FOUND"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"),

	// user
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER NOT FOUND"),
	USER_INACTIVITY(HttpStatus.FORBIDDEN, "USER STATUS IS INACTIVITY"),
	USER_BLOCKED(HttpStatus.FORBIDDEN, "USER STATUS IS BLOCKED"),
	FAIL_AUTHENTICATION(HttpStatus.BAD_REQUEST, "USER EMAIL OR PASSWORD IS NOT CORRECT"),
	INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "INVALID REFRESH TOKEN"),
	NOT_MATCHED_USER(HttpStatus.BAD_REQUEST, "REQUEST USER, AUTH USER IS NOT MATCH"),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "INVALID PASSWORD"),
	EXIST_USER(HttpStatus.BAD_REQUEST, "EXIST USER"),
	RECENT_USED_PASSWORD(HttpStatus.BAD_REQUEST, "RECENT USED PASSWORD"),

	// comment
	COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 게시글이 없거나 댓글이 없습니다."),
	COMMENT_NOT_USER(HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),

	// board
	POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 게시글이 없거나 댓글이 없습니다."),
	POST_NOT_USER(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.")
	;

	private final HttpStatus status;
	private final String message;
}