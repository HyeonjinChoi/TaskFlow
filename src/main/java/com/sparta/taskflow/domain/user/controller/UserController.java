package com.sparta.taskflow.domain.user.controller;

import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.domain.user.dto.*;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import com.sparta.taskflow.domain.user.service.UserService;
import com.sparta.taskflow.security.principal.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 사용자의 프로필 조회
    @GetMapping
    public ResponseEntity<CommonDto<UserProfileResDto>> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserProfileResDto responseDto = userService.getProfile(userDetails.getUser());
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                , "프로필 조회에 성공하였습니다!"
                , responseDto));
    }

    // 사용자의 프로필 수정
    @PutMapping
    public ResponseEntity<CommonDto<ProfileUpdateResDto>> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileUpdateReqDto reqDto) {
        ProfileUpdateResDto responseDto = userService.updateProfile(userDetails.getUser(), reqDto);
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                ,"프로필 수정에 성공하였습니다!"
                , responseDto));
    }

    @PutMapping("/password")
    public ResponseEntity<CommonDto<PasswordUpdateResDto>> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid PasswordUpdateReqDto reqDto) {
        PasswordUpdateResDto responseDto = userService.updatePassword(userDetails.getUser(), reqDto);
        return  ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                ,"비밀번호가 변경되었습니다."
                ,responseDto));
    }
}
