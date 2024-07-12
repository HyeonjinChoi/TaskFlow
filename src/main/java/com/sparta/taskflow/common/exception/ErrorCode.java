package com.sparta.taskflow.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// 기본
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),

	// 권한
	UNAUTHORIZED_ACTION(HttpStatus.FORBIDDEN, "권한이 없습니다."),

	// 사용자
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
	EXIST_USER(HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),
	USER_BLOCKED(HttpStatus.BAD_GATEWAY, "회원탈퇴한 유저입니다."),
	FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 정확하지 않습니다."),
	PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "현재 비밀번호와 사용자의 비밀번호가 일치하지 않습니다."),
	PASSWORD_REUSED(HttpStatus.BAD_REQUEST, "동일한 비밀번호로는 변경하실 수 없습니다."),

	// 보드
	BOARD_CREATE_MISSING_DATA(HttpStatus.BAD_REQUEST, "보드 이름과 한 줄 설명은 필수입니다."),
	BOARD_ALREADY_DELETED(HttpStatus.NOT_FOUND, "이미 삭제된 보드입니다."),
	BOARD_INVITE_ALREADY_MEMBER(HttpStatus.CONFLICT, "이미 해당 보드에 초대된 사용자입니다."),
	BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "보드를 찾을 수 없습니다."),

	// 섹션
	SECTION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 섹션 이름입니다."),
	SECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "섹션을 찾을 수 없습니다."),

	// 카드
	CARD_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 카드 이름입니다."),
	CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "카드를 찾을 수 없습니다."),

	// 댓글
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"댓글을 찾을 수 없습니다." );


	private final HttpStatus status;
	private final String message;
}