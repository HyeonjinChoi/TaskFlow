package com.sparta.taskflow.domain.auth.controller;

import com.sparta.taskflow.domain.auth.dto.LoginRequestDto;
import com.sparta.taskflow.domain.auth.dto.SignoutRequestDto;
import com.sparta.taskflow.domain.auth.dto.SignupRequestDto;
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
    public ResponseEntity<String> getProfile(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok().body(authService.signup(signupRequestDto));
    }

    // 회원 탈퇴
    @DeleteMapping("/signout")
    public ResponseEntity<String> getProfile(@Valid @RequestBody SignoutRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        return ResponseEntity.ok().body(authService.signout(requestDto,userPrincipal.getUser()));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto , HttpServletResponse httpResponse) {
        return ResponseEntity.ok().body(authService.login(requestDto,httpResponse));
    }

    // 로그아웃
    @DeleteMapping("/logout")
    public ResponseEntity<String> getProfile(@AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        return ResponseEntity.ok().body(authService.logout(userPrincipal.getUser()));
    }
}

