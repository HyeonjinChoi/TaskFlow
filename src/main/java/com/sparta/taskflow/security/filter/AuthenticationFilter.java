package com.sparta.taskflow.security.filter;

import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.security.principal.UserDetailsImpl;
import com.sparta.taskflow.security.service.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String TokenValue = jwtUtil.getFromTokenHeader(request);

        if(!StringUtils.hasText(TokenValue) || !TokenValue.startsWith(JwtUtil.TOKEN_AUTH_SCHEME)){
            filterChain.doFilter(request,response);
            return;
        }

        String accessToken = TokenValue.substring(7);

        if (!jwtUtil.isTokenValidate(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = jwtUtil.getUserFromToken(accessToken);
        UserDetailsImpl userPrincipal = new UserDetailsImpl(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }


}
