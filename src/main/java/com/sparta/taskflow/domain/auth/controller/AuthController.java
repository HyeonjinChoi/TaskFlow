package com.sparta.taskflow.domain.auth.controller;

import com.sparta.taskflow.domain.auth.dto.LoginRequestDto;
import com.sparta.taskflow.domain.auth.dto.SignoutRequestDto;
import com.sparta.taskflow.domain.auth.dto.SignupRequestDto;
import com.sparta.taskflow.domain.auth.dto.TokenResponseDto;
import com.sparta.taskflow.domain.auth.service.AuthService;
import com.sparta.taskflow.security.principal.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok().body(authService.signup(signupRequestDto));
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

    // 로그아웃
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@AuthenticationPrincipal UserDetailsImpl userPrincipal ,HttpServletResponse httpResponse) {
        return ResponseEntity.ok(authService.refresh(userPrincipal.getUser(),httpResponse));
    }

}

