package com.sparta.taskflow.domain.user.controller;

import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.domain.user.dto.*;
import com.sparta.taskflow.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // 사용자의 프로필 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<CommonDto<UserProfileResDto>> getProfile(@PathVariable Long userId) {
        UserProfileResDto responseDto = userService.getProfile(userId);
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                , "프로필 조회에 성공하였습니다!"
                , responseDto));
    }

    // 사용자의 프로필 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<CommonDto<ProfileUpdateResDto>> updateProfile(@PathVariable Long userId, @RequestBody ProfileUpdateReqDto reqDto) {
        ProfileUpdateResDto responseDto = userService.updateProfile(userId, reqDto);
        return ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                ,"프로필 수정에 성공하였습니다!"
                , responseDto));
    }

    @PutMapping("/users/{userId}/password")
    public ResponseEntity<CommonDto<PasswordUpdateResDto>> updatePassword(@PathVariable Long userId, @RequestBody PasswordUpdateReqDto reqDto) {
        PasswordUpdateResDto responseDto = userService.updatePassword(userId, reqDto);
        return  ResponseEntity.ok().body(new CommonDto<>(HttpStatus.OK.value()
                ,"비밀번호가 변경되었습니다."
                ,responseDto));
    }
}
