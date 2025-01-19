package com.ssmoker.smoker.domain.review.controller;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.service.MemberService;
import com.ssmoker.smoker.domain.review.dto.ReviewGetResponse;
import com.ssmoker.smoker.domain.review.dto.ReviewRequest;
import com.ssmoker.smoker.domain.review.service.ReviewService;
import com.ssmoker.smoker.domain.smokingArea.dto.ReviewResponse;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.security.handler.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;

    @Operation(summary = "흡연 구역 리뷰 작성")
    @PostMapping("/{smokingAreaId}")
    public ApiResponse<Long> createReview(@PathVariable Long smokingAreaId,
                                          @RequestBody @Valid ReviewRequest reviewRequest,
                                          @AuthUser Long memberId) {

        Member member = memberService.findMemberById(memberId);

        Long reviewId = reviewService.saveReview(smokingAreaId, reviewRequest, member);
        return ApiResponse.onSuccess(reviewId);
    }

    @Operation(summary = "리뷰 작성 완료 페이지")
    @GetMapping("/complete/{reviewId}")
    public ApiResponse<ReviewGetResponse> getReviewComplete(@PathVariable Long reviewId) {
        ReviewGetResponse reviewResponse = reviewService.getReviewById(reviewId);
        return ApiResponse.onSuccess(reviewResponse);
    }
}
