package com.ssmoker.smoker.domain.member.controller;

import com.ssmoker.smoker.domain.member.dto.MemberRequestDTO;
import com.ssmoker.smoker.domain.member.dto.MemberResponseDTO;
import com.ssmoker.smoker.domain.member.service.MemberService;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.apiPayload.code.SuccessStatus;
import com.ssmoker.smoker.global.security.handler.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @Operation(summary = "프로필(프로필 사진 url, 닉네임) 조회 API", description = "사용자 프로필을 조회합니다.")
    @GetMapping(value = "/")
    public ApiResponse<MemberResponseDTO.MemberProfileDTO> getProfile(@AuthUser Long memberId) {
        MemberResponseDTO.MemberProfileDTO result = memberService.viewProfile(memberId);
        return ApiResponse.of(SuccessStatus.PROFILE_OK,result);
    }

    @Operation(summary = "마이페이지 내가 쓴 리뷰 조회 API", description = "마이페이지 리뷰탭 내가 쓴 리뷰를 5개씩 조회합니다. 쿼리스트링으로 pageNumber를 넘겨주세요. 1부터 시작됩니다.")
    @GetMapping(value = "/reviews")
    public ApiResponse<MemberResponseDTO.MemberReviewListDTO> getMemberReview(@AuthUser Long memberId, @RequestParam("pageNumber") @Min(0) @NotNull Integer pageNumber) {
        MemberResponseDTO.MemberReviewListDTO result = memberService.viewMemberReviews(memberId,pageNumber);
        return ApiResponse.of(SuccessStatus.PROFILE_REVIEWS_OK,result);
    }

    @Operation(summary = "마이페이지 나의 업데이트 히스토리 조회 API", description = "마이페이지 상세탭 나의 업데이트 히스토리를 5개씩 조회합니다. 쿼리스트링으로 pageNumber를 넘겨주세요. 1부터 시작됩니다.")
    @GetMapping(value = "/update")
    public ApiResponse<MemberResponseDTO.MemberUpdateListDTO> getMemberUpdate(@AuthUser Long memberId, @RequestParam("pageNumber") @Min(0) @NotNull Integer pageNumber) {
        MemberResponseDTO.MemberUpdateListDTO result = memberService.viewMemberUpdateHistory(memberId,pageNumber);
        return ApiResponse.of(SuccessStatus.PROFILE_UPDATE_OK,result);
    }
}