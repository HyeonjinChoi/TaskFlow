package com.sparta.taskflow.domain.user.service;

import com.sparta.taskflow.common.exception.BusinessException;
import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.domain.user.dto.*;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        User users = getUser(user);
        users.update(profileUpdateReqDto);
        return new ProfileUpdateResDto(users);
    }

    // 비밀번호 변경
    @Transactional
    public PasswordUpdateResDto updatePassword(User user, PasswordUpdateReqDto passwordUpdateReqDto) {

        User users = getUser(user);
        String password = users.getPassword();

        String currentPassword = passwordUpdateReqDto.getCurrentPassword();
        String newPassword = passwordUpdateReqDto.getNewPassword();

        // 현재 비밀번호가 사용자의 비밀번호와 맞는지 검증
        if (!passwordEncoder.matches(currentPassword, password)) {
            throw new BusinessException(ErrorCode.PASSWORD_INCORRECT);
        }

        // 변경할 비밀번호와 현재 비밀번호가 동일한지 검증
        if (passwordEncoder.matches(newPassword, password)) {
            throw new BusinessException(ErrorCode.PASSWORD_INCORRECT);
        }

        // 변경할 비밀번호로 수정
        users.passwordUpdate(passwordEncoder.encode(newPassword));

        return new PasswordUpdateResDto(users);
    }

    private User getUser(User user) {
        User users = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return users;
    }
}