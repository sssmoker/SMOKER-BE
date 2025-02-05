package com.ssmoker.smoker.domain.token.controller;

import com.ssmoker.smoker.global.security.authDTO.AuthResponseDTO.TokenResponse;
import com.ssmoker.smoker.domain.token.service.TokenService;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {

    private final TokenService tokenService;

    @Operation(
            summary = "JWT Access Token 재발급 API",
            description = "Refresh Token 을 검증하고 새로운 Access Token 과 Refresh Token 을 응답합니다.")
    @PostMapping("/reissue")
    public ApiResponse<TokenResponse> reissueToken(final HttpServletRequest request) {
        TokenResponse tokenResponse = tokenService.reissueToken(request);

        return ApiResponse.onSuccess(tokenResponse);
    } // Access Token 재발급

    @Operation(
            summary = "로그아웃 API",
            description = "Refresh Token을 삭제합니다.")
    @GetMapping("/logout")
    public void logout(final HttpServletRequest request) {
        tokenService.deleteToken(request);
    } // 로그아웃
}
