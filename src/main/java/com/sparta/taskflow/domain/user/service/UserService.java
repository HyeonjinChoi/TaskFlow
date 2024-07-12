package com.sparta.taskflow.domain.user.service;

import com.sparta.taskflow.domain.user.dto.*;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 프로필 조회
    public UserProfileResDto getProfile(User user) {
        UserProfileResDto responseDto = new UserProfileResDto(user);
        return responseDto;
    }

    // 사용자 프로필 수정
    @Transactional
    public ProfileUpdateResDto updateProfile(User user, ProfileUpdateReqDto profileUpdateReqDto) {
        user.update(profileUpdateReqDto);
        return new ProfileUpdateResDto(user);
    }

    // 비밀번호 변경
    @Transactional
    public PasswordUpdateResDto updatePassword(User user, PasswordUpdateReqDto passwordUpdateReqDto) {

        String currentPassword = passwordUpdateReqDto.getCurrentPassword();
        String newPassword = passwordUpdateReqDto.getNewPassword();

        // 현재 비밀번호가 사용자의 비밀번호와 맞는지 검증
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호와 사용자의 비밀번호가 일치하지 않습니다.");
        }

        // 변경할 비밀번호와 현재 비밀번호가 동일한지 검증
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("동일한 비밀번호로는 변경할 수 없습니다.");
        }

        // 변경할 비밀번호로 수정
        user.passwordUpdate(passwordEncoder.encode(newPassword));

        return new PasswordUpdateResDto(user);
    }
}
