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
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾지 못했습니다."),
	EXIST_USER(HttpStatus.NOT_FOUND, "이미 존제하는 유저입니다."),
	USER_BLOCKED(HttpStatus.BAD_GATEWAY,"회원탈퇴한 유저입니다." ),
	FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 정확하지 않습니다.");


	// comment
//	COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 게시글이 없거나 댓글이 없습니다."),




	private final HttpStatus status;
	private final String message;
}