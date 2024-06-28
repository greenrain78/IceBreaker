package com.example.IceBreaking.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // h2-console 접근을 위한 설정
        http.headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
        );

        // 인가
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico").permitAll() // 정적 리소스 접근 허용
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // swagger 접근 허용
                .requestMatchers("/h2-console/**").permitAll() // h2-console 접근 허용
                .requestMatchers("/", "login", "/sign-up",  "/loginProc", "/join", "/joinProc").permitAll() // 로그인, 회원가입 페이지 접근 허용
                .requestMatchers("/admin").hasRole("ADMIN") // ADMIN 권한을 가진 유저에 대해 접근 허용
                .requestMatchers("/new").hasAnyRole("ADMIN", "USER") // ADMIN, USER 권한을 가진 유저에 대해 접근 허용
                .anyRequest().authenticated()   // 모든 요청에 대해 인증을 요구
        );

        // 인증
        http.formLogin((auth) -> auth.loginPage("/login")   // 비인가 사용자가 접근 시 login 페이지로 이동
                        .loginProcessingUrl("/loginProc")   // login 페이지에서 로그인 버튼 클릭 시 loginProc으로 요청
                        .permitAll()
        );

        // 로그아웃 설정
        http.logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")  // 로그아웃 성공 시 /로 리다이렉트
                .permitAll()
        );
        // csrf 보안 설정 비활성화 - post 요청 시 csrf 토큰을 요구하는데, 이를 비활성화
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
