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
    /**
     * 가독성을 위해 api 요청중에서 로그인이 필요없는 것 url 많은 api 들은 일단 전부 허용되게 처리해뒀고(/**)
     * 해당 api 에서 로그인이 필요한 것이 있으면 configureAuthorization 에 해당 url 추가하시면 됩니다.
     */
    private static final String ALLOW_REVIEW_URL = "/api/reviews/**";
    private static final String ALLOW_SMOKING_AREA_URL = "/api/smoking-area/**";
    private static final String ALLOW_AUTH_URL = "/api/auth/**";
    private static final String ALLOW_OPEN_API = "/api/open-api/**";

    private static final String[] SECURITY_ALLOW_ARRAY = { //인증 없이 접근 가능한 엔드 포인트
            ALLOW_OPEN_API,
            ALLOW_AUTH_URL,
            ALLOW_SMOKING_AREA_URL,
            ALLOW_REVIEW_URL,
            "/api/member/notices", //공지사항
            "/test/**",
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
            "/favicon.ico"
    };

    private final JwtRequestFilter jwtRequestFilter;

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
            response.getWriter().write("{ \"error\": \"Access Denied\", \"message\": \"권한이 없습니다.\", \"path\": \""
                    + request.getRequestURI() + "\" }");
        };
    }

}
