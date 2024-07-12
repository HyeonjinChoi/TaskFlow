package com.sparta.taskflow.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.taskflow.security.exception.AccessDeniedHandlerImpl;
import com.sparta.taskflow.security.exception.AuthenticationEntryPointImpl;
import com.sparta.taskflow.security.filter.AuthenticationFilter;
import com.sparta.taskflow.security.filter.TransactionFilter;
import com.sparta.taskflow.security.service.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFilter authenticationFilter(JwtUtil jwtUtil) {
        return new AuthenticationFilter(jwtUtil);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.exceptionHandling(handler -> {
                                   handler.accessDeniedHandler(new AccessDeniedHandlerImpl(objectMapper));
                                   handler.authenticationEntryPoint(new AuthenticationEntryPointImpl(objectMapper));
                               }
        );

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                .anyRequest().authenticated()
        );

        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterAt(new TransactionFilter(), BasicAuthenticationFilter.class);
        http.addFilterAt(authenticationFilter(jwtUtil), TransactionFilter.class);

        return http.build();
    }
}
