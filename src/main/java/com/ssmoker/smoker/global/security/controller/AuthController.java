package com.ssmoker.smoker.global.security.controller;

import com.ssmoker.smoker.domain.token.service.TokenService;
import com.ssmoker.smoker.global.security.authDTO.AuthResponseDTO;
import com.ssmoker.smoker.global.security.authDTO.AuthResponseDTO.OAuthResponse;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.apiPayload.code.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인 및 회원 가입을 진행하는 API입니다. 인가코드를 넘겨주세요")
    @GetMapping("/login/kakao")
    public ApiResponse<OAuthResponse> kakaoLogin(@RequestParam("code") String code) {
        return ApiResponse.of(SuccessStatus.USER_LOGIN_OK, authService.kakaoLogin(code));
    } //카카오 로그인

    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "구글 로그인 API", description = "구글 로그인 및 회원 가입을 진행하는 API입니다. 인가코드를 넘겨주세요")
    @GetMapping("/login/google")
    public ApiResponse<AuthResponseDTO.OAuthResponse> googleLogin(@RequestParam("code") String code) {
        return ApiResponse.of(SuccessStatus.USER_LOGIN_OK, authService.GoogleLogin(code));
    } //구글 로그인

    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 API 입니다")
    @DeleteMapping("/deactivate")
    public ApiResponse<Void> AccountDeactivation(HttpServletRequest request) {
        tokenService.deleteUser(request);
        return ApiResponse.of(SuccessStatus.USER_DELETE_OK,null);
    } //회원 탈퇴
}
