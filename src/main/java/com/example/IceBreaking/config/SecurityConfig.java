package com.example.IceBreaking.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // swagger-ui, api-docs에 대해 접근 허용
                .requestMatchers("/", "login", "/sign-up").permitAll() // /, login, sign-up에 대해 접근 허용
                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER") // ADMIN, USER 권한을 가진 유저에 대해 접근 허용
                .anyRequest().authenticated()   // 모든 요청에 대해 인증을 요구
        );
        return http.build();
    }
}
