package com.ssmoker.smoker.global.security.filter;

import com.ssmoker.smoker.global.security.principal.PrincipalDetailsService;
import com.ssmoker.smoker.global.security.provider.JwtTokenProvider;
import com.ssmoker.smoker.global.security.exception.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final PrincipalDetailsService principalDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String path = request.getRequestURI();

        // 1) 만약 Swagger, 로그인 등 "인증이 필요 없는 경로"이면 → 토큰 검증 스킵
        if (isPermitAllPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtTokenProvider.extractToken(request);

            if (jwtTokenProvider.isTokenValid(token)) {
                Long userId = jwtTokenProvider.getId(token);
                UserDetails userDetails =
                        principalDetailsService.loadUserByUsername(userId.toString());

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, "", userDetails.getAuthorities());
                    SecurityContextHolder.getContext()
                            .setAuthentication(usernamePasswordAuthenticationToken);
                } else { //유저 없음
                    throw new AuthException(ErrorStatus.USER_NOT_FOUND);
                }
            } else { //토큰이 유효하지 않음
                throw new AuthException(ErrorStatus.AUTH_INVALID_TOKEN);
            }

            filterChain.doFilter(request, response);
        } catch (AuthException ex) {
            setJsonResponse(response, ex.getErrorReasonHttpStatus().getHttpStatus().value(),
                    ex.getErrorReason().getCode(),
                    ex.getErrorReason().getMessage());
        } catch (Exception ex) {
            setJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "INTERNAL_SERVER_ERROR",
                    "예기치 않은 오류가 발생했습니다.");
        }
    }

    // JSON 응답 설정
    private void setJsonResponse(HttpServletResponse response, int statusCode, String code, String message)
            throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = String.format("{\"isSuccess\": false, \"code\": \"%s\", \"message\": \"%s\"}", code,
                message);
        response.getWriter().write(jsonResponse);
    }

    private boolean isPermitAllPath(String path) {
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")
                || path.equals("/swagger-ui.html")
                || path.equals("/health")
                || path.startsWith("/api/auth/login/")

                // test
                || path.startsWith("/test")
                // fixme : 차라리 관리자 권한을 가진 유저만 호출할 수 있도록 변경해야할 듯
                || path.startsWith("/api/open-api/")
                //smokingArea
                || path.startsWith("/api/smoking-area/marker")
                || path.startsWith("/api/smoking-area/{smokingAreaId}/simple")
                || path.startsWith("/api/smoking-area/list")
                || path.startsWith("/api/smoking-area/search")

                //Review
                || path.startsWith("/api/reviews/{smokingAreaId}")
                || path.startsWith("/api/reviews/{smokingAreaId}/starInfo")

                //Notice
                || path.startsWith("/api/member/notices")
                || path.startsWith("/api/member/notices/detail/{noticeId}")

                // 필요하다면 다른 permitAll 경로들도 추가
                // ...
                ;
    }
}
