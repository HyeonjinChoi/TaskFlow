package com.sparta.taskflow.domain.auth.service;

import com.sparta.taskflow.common.config.RestTemplateConfig;
import com.sparta.taskflow.common.dto.CommonDto;
import com.sparta.taskflow.common.exception.BusinessException;
import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.domain.auth.dto.*;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import com.sparta.taskflow.security.principal.UserDetailsImpl;
import com.sparta.taskflow.security.service.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplateConfig restTemplate;
    private final JwtUtil jwtUtil;

    public CommonDto<SignupResponsDto> signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        String nickname = signupRequestDto.getNickname();
        String introduction = signupRequestDto.getIntroduction();
        String RolePassword = signupRequestDto.getRolePassword();

        String roles = "67mE67CA67KI7Zi4";
        User.Role role = User.Role.USER;
        String message = "회원가입에 성공하였습니다.";


        if (userRepository.existsByEmailOrNicknameOrUsername(username, email, nickname))
            throw new BusinessException(ErrorCode.EXIST_USER);

        if(RolePassword.equals(roles)){
            role = User.Role.MANAGER;
        }
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .introduction(introduction)
                .role(role)
                .status(User.Status.NORMAL)
                .build();
        userRepository.save(user);


        if(RolePassword.equals(roles)){
            message =   "매니저님 회원가입이 완료되었습니다.";
        }

        SignupResponsDto responseDto = new SignupResponsDto(user);

        return  new CommonDto<>(HttpStatus.OK.value(), message, responseDto);
    }

    @Transactional
    public String signout(SignoutRequestDto requestDto, User user) {
        User users = userRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getPassword(), users.getPassword())) {
            throw new BusinessException(ErrorCode.FAIL_AUTHENTICATION);
        }
        users.signout();
        return "회원탈퇴가 완료되었습니다. 그 동안 감사했습니다.";
    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto requestDto , HttpServletResponse httpResponse) {
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

            return new TokenResponseDto(accessToken);
        } catch (LockedException e) {
            throw new BusinessException(ErrorCode.USER_BLOCKED);
        }  catch (AuthenticationException e) {
            throw new BusinessException(ErrorCode.FAIL_AUTHENTICATION);
        }
    }

    @Transactional
    public String logout(User user) {
        User logout = userRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        logout.deleteRefreshToken();
        return "로그아웃이 완료되었습니다.";
    }


    @Transactional
    public String refreshToken(String token) {
        User user = userRepository.findById(jwtUtil.getUserFromToken(token).getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(jwtUtil.isTokenValidate(user.getRefreshToken().substring(7))) {
            String accessToken = jwtUtil.createAccessToken(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getStatus(), user.getNickname());
            return accessToken;
        } else {
            return login(user.getUsername(), user.getPassword());
        }
    }

    @Value("http://localhost:8080")
    private String authServerUrl;

    private String login(String username, String password) {
        String loginUrl = authServerUrl + "/api/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginRequestDto requestDto = LoginRequestDto.builder()
                .username(username)
                .password(password)
                .build();
        HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<TokenResponseDto> response = restTemplate.restTemplate().exchange(loginUrl, HttpMethod.POST, requestEntity, TokenResponseDto.class);

        return response.getBody().getAccessToken();
    }

}
