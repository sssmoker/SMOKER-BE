package com.ssmoker.smoker.domain.review.controller;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.review.dto.ReviewRequestDTO;
import com.ssmoker.smoker.domain.review.service.ReviewService;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "흡연 구역 리뷰 작성")
    @PostMapping("/{smokingAreaId}")
    public ApiResponse<Long> createReview(@PathVariable Long smokingAreaId,
                                          @RequestBody @Valid ReviewRequestDTO reviewRequestDTO,
                                          @AuthenticationPrincipal Member authenticatedMember) {

        reviewRequestDTO.setSmokingAreaId(smokingAreaId);
        reviewRequestDTO.setMemberId(authenticatedMember.getId());

        Long reviewId = reviewService.saveReview(reviewRequestDTO);
        return ApiResponse.onSuccess(reviewId);
    }
}
