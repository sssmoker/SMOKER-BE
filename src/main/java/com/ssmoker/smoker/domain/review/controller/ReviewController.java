package com.ssmoker.smoker.domain.review.controller;

import com.ssmoker.smoker.domain.review.dto.ReviewStarsInfoResponse;
import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.service.MemberService;
import com.ssmoker.smoker.domain.review.dto.ReviewGetResponse;
import com.ssmoker.smoker.domain.review.dto.ReviewRequest;
import com.ssmoker.smoker.domain.review.service.ReviewService;
import com.ssmoker.smoker.domain.review.dto.ReviewResponses;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.security.handler.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews/")
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;

    @Operation(summary = "흡연 구역 상세 페이지 - 리뷰 조회(최신순)", description = "쿼리 스트링으로 원하는 페이지를 넘겨주시면 됩니다.")
    @GetMapping("/{smokingAreaId}")
    public ApiResponse<ReviewResponses> getReviews(@PathVariable Long smokingAreaId,
                                                   @RequestParam @Min(0) @NotNull Integer pageNumber) {
        ReviewResponses result = reviewService.getReviewsBySmokingAreaId(smokingAreaId, pageNumber);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "흡연 구역 상세 페이지 - 총 별점 및 별점 개수 조회",
            description = "흡연 구역 별 별점 평균 및 전체 별점 조회입니다. 전체 별점은 리스트로 1->5 점으로 개수가 반환됩니다.")
    @GetMapping("/{smokingAreaId}/starInfo")
    public ApiResponse<ReviewStarsInfoResponse> getSmokingAreaStarInfo(@PathVariable Long smokingAreaId) {
        ReviewStarsInfoResponse result = reviewService.getStarsInfoBySmokingAreaId(smokingAreaId);

        return ApiResponse.onSuccess(result);
    }

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
