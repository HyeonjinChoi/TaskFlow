package com.sparta.taskflow.domain.auth.service;

import com.sparta.taskflow.common.exception.BusinessException;
import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.domain.auth.dto.LoginRequestDto;
import com.sparta.taskflow.domain.auth.dto.SignoutRequestDto;
import com.sparta.taskflow.domain.auth.dto.SignupRequestDto;
import com.sparta.taskflow.domain.auth.dto.TokenResponseDto;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import com.sparta.taskflow.security.principal.UserDetailsImpl;
import com.sparta.taskflow.security.service.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        String nickname = signupRequestDto.getNickname();
        String introduction = signupRequestDto.getIntroduction();
        String RolePassword = signupRequestDto.getRolePassword();

        String roles = "67mE67CA67KI7Zi4";
        User.Role role = User.Role.USER;

        if (userRepository.existsByEmailOrNicknameOrUsername(username, email, nickname))
            throw new BusinessException(ErrorCode.EXIST_USER);

        if(RolePassword.equals(roles)){
            role = User.Role.MANAGER;
        }

        userRepository.save(User.builder()
                                    .username(username)
                                    .email(email)
                                    .password(passwordEncoder.encode(password))
                                    .nickname(nickname)
                                    .introduction(introduction)
                                    .role(role)
                                    .status(User.Status.NORMAL)
                                    .build());


        if(RolePassword.equals(roles)){
            return  "매니저님 회원가입이 완료되었습니다.";
        }
        return  "유저님 회원가입이 완료되었습니다.";
    }

    public String signout(SignoutRequestDto requestDto, User user) {
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.FAIL_AUTHENTICATION);
        }
        user.signout();
        return "회원탈퇴가 완료되었습니다. 그 동안 감사했습니다.";
    }

    @Transactional
    public String login(LoginRequestDto requestDto , HttpServletResponse httpResponse) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );

            UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
            User user = userPrincipal.getUser();

            String accessToken = jwtUtil.createAccessToken(user.getId(), user.getUsername(),user.getEmail(), user.getRole(), user.getStatus(), user.getNickname());
            String refreshToken = jwtUtil.createRefreshToken();

            user.addRefreshToken(refreshToken);
            httpResponse.addHeader(HttpHeaders.AUTHORIZATION, accessToken);

            return "로그인이 완료되었습니다.";
        } catch (LockedException e) {
            throw new BusinessException(ErrorCode.USER_BLOCKED);
        }  catch (AuthenticationException e) {
            throw new BusinessException(ErrorCode.FAIL_AUTHENTICATION);
        }
    }

    @Transactional
    public String logout(User user) {
        user.deleteRefreshToken();
        return "로그아웃이 완료되었습니다.";
    }
}
