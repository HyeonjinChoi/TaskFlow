package com.sparta.taskflow.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")  // 허용할 Origin 설정
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP Methods 설정
                .allowCredentials(true);  // 필요시, credential 허용
    }

    // CORS 필터 빈 등록 (선택적)
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");  // 모든 Origin 허용
        config.addAllowedHeader("*");  // 모든 Header 허용
        config.addAllowedMethod("*");  // 모든 HTTP Methods 허용
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
