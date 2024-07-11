package com.sparta.taskflow.domain.auth.controller;

import com.sparta.taskflow.domain.auth.dto.LoginDto;
import com.sparta.taskflow.domain.auth.dto.SignoutRequestDto;
import com.sparta.taskflow.domain.auth.dto.SignupRequestDto;
import com.sparta.taskflow.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;

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
    public ResponseEntity<String> getProfile(@Valid @RequestBody SignoutRequestDto requestDto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok().body(authService.signout(requestDto,userPrincipal));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> getProfile(@RequestBody LoginDto requestDto) {
        return ResponseEntity.ok().body(authService.login(requestDto));
    }

    // 로그아웃
    @DeleteMapping("/logout")
    public ResponseEntity<String> getProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok().body(authService.logout(userPrincipal));
    }
}
