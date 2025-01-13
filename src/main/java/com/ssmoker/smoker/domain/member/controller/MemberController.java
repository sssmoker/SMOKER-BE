package com.ssmoker.smoker.domain.member.controller;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.dto.AuthRequestDTO;
import com.ssmoker.smoker.domain.member.dto.AuthResponseDTO;
import com.ssmoker.smoker.domain.member.service.MemberService;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.apiPayload.code.SuccessStatus;
import com.ssmoker.smoker.security.handler.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class MemberController {
    private final MemberService memberService;

    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인 및 회원 가입을 진행하는 API입니다. 인가코드를 넘겨주세요")
    @GetMapping("/login/kakao")
    public ApiResponse<AuthResponseDTO.OAuthResponse> kakaoLogin(@RequestParam("code") String code) {
        return ApiResponse.of(SuccessStatus.USER_LOGIN_OK, memberService.kakaoLogin(code));
    } //카카오 로그인

    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "구글 로그인 API", description = "구글 로그인 및 회원 가입을 진행하는 API입니다. 인가코드를 넘겨주세요")
    @GetMapping("/login/google")
    public ApiResponse<AuthResponseDTO.OAuthResponse> googleLogin(@RequestParam("code") String code) {
        return ApiResponse.of(SuccessStatus.USER_LOGIN_OK, memberService.GoogleLogin(code));
    } //구글 로그인

    @ResponseStatus(code = HttpStatus.OK)
    @Operation(
            summary = "JWT Access Token 재발급 API",
            description = "Refresh Token을 검증하고 새로운 Access Token과 Refresh Token을 응답합니다.")
    @PostMapping("/refresh")
    public ApiResponse<AuthResponseDTO.TokenRefreshResponse> refresh(@RequestBody AuthRequestDTO.RefreshTokenDTO request) {
        return ApiResponse.of(SuccessStatus.USER_REFRESH_OK, memberService.refresh(request.getRefreshToken()));
    } // JWT Access Token 재발급

/*
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(
            summary = "로그아웃 API",
            description = "로그아웃 합니다.")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Parameter(name = "user", hidden = true) @AuthUser Member member) {
        memberService.logout(member);
        return ApiResponse.of(SuccessStatus.USER_LOGOUT_OK,null);
    } // 로그아웃

    @ResponseStatus(code = HttpStatus.OK)
    @Operation(
            summary = "회원 탈퇴 API",
            description = "회원 탈퇴를 합니다.")
    @PostMapping("/deactivate")
    public ApiResponse<Void> deactivate(@Parameter(name = "user", hidden = true) @AuthUser Member member) {
        memberService.deactivate(member);
        return ApiResponse.of(SuccessStatus.USER_DELETE_OK,null);
    } // 회원 탈퇴
*/

}