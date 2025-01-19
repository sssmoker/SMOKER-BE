package com.ssmoker.smoker.domain.member.controller;

import com.ssmoker.smoker.domain.member.dto.MemberRequestDTO;
import com.ssmoker.smoker.domain.member.service.MemberService;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.apiPayload.code.SuccessStatus;
import com.ssmoker.smoker.global.security.handler.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/member")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "닉네임 변경 API", description = "닉네임을 변경합니다.")
    @PatchMapping("/nickname")
    public ApiResponse<Void> updateNickname(@AuthUser Long memberId, @RequestBody MemberRequestDTO.updateNicknameRequestDTO request) {
        memberService.updateNickname(memberId, request.getNickname());
        return ApiResponse.of(SuccessStatus.NICKNAME_OK,null);
    }
}