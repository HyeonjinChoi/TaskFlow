package com.sparta.taskflow.domain.auth.controller;

import com.sparta.taskflow.domain.auth.dto.LoginRequestDto;
import com.sparta.taskflow.domain.auth.dto.SignoutRequestDto;
import com.sparta.taskflow.domain.auth.dto.SignupRequestDto;
import com.sparta.taskflow.domain.auth.service.AuthService;
import com.sparta.taskflow.security.principal.UserDetailsImpl;
import com.sparta.taskflow.security.service.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<String> getProfile(@Valid @RequestBody SignoutRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        return ResponseEntity.ok().body(authService.signout(requestDto,userPrincipal.getUser()));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {
        String accessToken = authService.login(requestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, JwtUtil.TOKEN_AUTH_SCHEME + accessToken);

        return ResponseEntity.ok()
                .headers(headers)
                .body("로그인이 완료 되었습니다.");
    }

    // 로그아웃
    @DeleteMapping("/logout")
    public ResponseEntity<String> getProfile(@AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        return ResponseEntity.ok().body(authService.logout(userPrincipal.getUser()));
    }
}
