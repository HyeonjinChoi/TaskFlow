package com.sparta.taskflow.common.aop;

import com.sparta.taskflow.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class TokenAspect {

    @Autowired
    private AuthService authService;

    @Pointcut("@annotation(com.sparta.taskflow.common.aop.TokenUpdateRequired)")
    public void tokenUpdatePointcut() {}

    @Before("tokenUpdatePointcut()")
    public void updateToken() throws Throwable {
        String updatedToken = "";
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization").substring(7);

        if (token != null && !token.isEmpty()) {
            updatedToken = authService.refreshToken(token);
        }

        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        if (response != null && updatedToken != null) {
            response.setHeader("Authorization", updatedToken);
        }
    }
}
