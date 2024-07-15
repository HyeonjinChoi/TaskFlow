package com.sparta.taskflow.domain.auth.controller;

import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.domain.auth.dto.*;
import com.sparta.taskflow.domain.auth.service.AuthService;
import com.sparta.taskflow.domain.comment.dto.CommentResponseDto;
import com.sparta.taskflow.security.principal.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {

        CommonDto<SignupResponsDto> response = authService.signup(signupRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 회원 탈퇴
    @PutMapping("/signout")
    public ResponseEntity<String> signout(@Valid @RequestBody SignoutRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        return ResponseEntity.ok().body(authService.signout(requestDto,userPrincipal.getUser()));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto requestDto , HttpServletResponse httpResponse) {
        return ResponseEntity.ok(authService.login(requestDto,httpResponse));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        return ResponseEntity.ok().body(authService.logout(userPrincipal.getUser()));
    }

}

