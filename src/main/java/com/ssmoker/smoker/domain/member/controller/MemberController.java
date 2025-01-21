package com.ssmoker.smoker.domain.member.controller;

import com.ssmoker.smoker.domain.member.dto.MemberRequestDTO;
import com.ssmoker.smoker.domain.member.service.MemberService;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.apiPayload.code.SuccessStatus;
import com.ssmoker.smoker.global.security.handler.annotation.AuthUser;
import com.ssmoker.smoker.test.TestRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @Operation(summary = "프로필 사진 변경 API", description = "프로필 사진을 변경합니다.")
    @PatchMapping(value = "/profileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateProfileImage(@AuthUser Long memberId, @ModelAttribute MemberRequestDTO.updateProfileImageRequestDTO request) {
        String imageUrl = memberService.updateProfileImage(memberId, request);
        return ApiResponse.of(SuccessStatus.PROFILE_IMAGE_OK,imageUrl);
    }
}