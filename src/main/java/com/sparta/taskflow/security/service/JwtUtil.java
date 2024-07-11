package com.sparta.taskflow.security.service;


import com.sparta.taskflow.domain.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    public static final String TOKEN_HEADER = "AccessToken";
    public static String TOKEN_AUTH_SCHEME = "Bearer ";
    private final Long AccessTokenTime = 60 * 30 * 1000L;
    private final Long RefreshTokenTime = 24 * 30 * 60 * 60 * 1000L;

    public static String CLAIM_ID = "id";
    public static String CLAIM_NICKNAME = "nickname";
    public static String CLAIM_EMAIL = "email";
    public static String CLAIM_ROLE = "role";
    public static String CLAIM_STATUS = "status";
    private final Logger log = LoggerFactory.getLogger(JwtUtil.class.getName());

    @Value("${jwt.secret-key}")
    private String SecretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(SecretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(Long id, String username,String email ,User.Role role, User.Status status, String nickname) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + AccessTokenTime))
                .claim(CLAIM_ID,id)
                .claim(CLAIM_EMAIL, email)
                .claim(CLAIM_ROLE,role)
                .claim(CLAIM_STATUS,status)
                .claim(CLAIM_NICKNAME,nickname)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public String createRefreshToken() {
        Date date = new Date();
        return Jwts.builder()
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime()+ RefreshTokenTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getFromTokenHeader(HttpServletRequest request){
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    public boolean isTokenValidate(String token) {
        try { Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public User getUserFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return User.builder()
                .id(claims.get(CLAIM_ID, Long.class))
                .username(claims.getSubject())
                .nickname(claims.get(CLAIM_NICKNAME, String.class))
                .role(User.Role.valueOf(claims.get(CLAIM_ROLE, String.class)) )
                .status( User.Status.valueOf(claims.get(CLAIM_STATUS, String.class)))
                .build();
    }
}
