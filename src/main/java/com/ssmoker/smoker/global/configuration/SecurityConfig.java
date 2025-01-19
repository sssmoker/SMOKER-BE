package com.ssmoker.smoker.global.configuration;

import com.ssmoker.smoker.global.security.filter.JwtRequestFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    private static final String[] SECURITY_ALLOW_ARRAY  = { //인증 없이 접근 가능한 엔드 포인트
            "/api/auth/login/google", //구글 로그인
            "/api/auth/login/kakao", //카카오 로그인
            "/api/auth/refresh", //토큰 재발급
            "/api/member/notices", //공지사항
            "/api/smoking-area/{smokingAreaId}", //흡연 구역 상세 정보 조회
            "/api/smoking-area/{smokingAreaId}/reviews", // 흡연 구역 리뷰 조회
            "/api/updated_history/{smokingAreaId}", // 업데이트 히스토리 조회
            "/api/smoking-area/{smokingAreaId}/rating_info", //총 별점 & 별점 개수 조회
            "/api/smoking-area", //흡연구역 마커 보이기
            "/api/smoking-area/{smokingAreaId}/simple", //흡연 구역 지도 마커 클릭했을 때 흡연 구역 정보 조회 (모달)
            "/api/smoking-area/list", //흡연구역 목록보기
            "/api/smoking-area/search", //흡연 구역 검색
            "/test/{status}", //테스트 컨트롤러
            //등등 추가 및 수정해주세요
            "/health",
            "/error",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/login",
            "/refresh",
            "/actuator/prometheus",
            "/ws/**",
            "/topic/**",
            "/api/post/**",
            "/api/recommend/**",
            "/favicon.ico"
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        configureAuthorization(http);
        configureExceptionHandling(http);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private void configureAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(SECURITY_ALLOW_ARRAY).permitAll()
                .anyRequest().hasAnyAuthority("ROLE_USER")
        );
    }

    private void configureExceptionHandling(HttpSecurity http) throws Exception {
        http.exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{ \"error\": \"Unauthorized\", \"message\": \"인증이 필요합니다.\" }");
                })
        );
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{ \"error\": \"Access Denied\", \"message\": \"권한이 없습니다.\", \"path\": \"" + request.getRequestURI() + "\" }");
        };
    }

}
