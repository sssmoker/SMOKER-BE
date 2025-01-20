package com.ssmoker.smoker.domain.review.controller;

import com.ssmoker.smoker.domain.review.dto.ReviewStarsInfoResponse;
import com.ssmoker.smoker.domain.review.service.ReviewService;
import com.ssmoker.smoker.domain.review.dto.ReviewResponses;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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

    @Operation(summary = "흡연 구역 상세 페이지 - 리뷰 조회(최신순)", description = "쿼리 스트링으로 원하는 페이지를 넘겨주시면 됩니다.")
    @GetMapping("/{smokingAreaId}")
    public ApiResponse<ReviewResponses> getReviews(@PathVariable Long smokingAreaId,
                                                   @RequestParam @Min(0) @NotNull Integer pageNumber) {
        ReviewResponses result = reviewService.getReviewsBySmokingAreaId(smokingAreaId, pageNumber);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "흡연 구역 상세 페이지 - 총 별점 및 별점 개수 조회",
            description = "흡연 구역 별 별점 평균 및 전체 별점 조회")
    @GetMapping("/{smokingAreaId}/stars")
    public ApiResponse<ReviewStarsInfoResponse> getSmokingAreaStarInfo(@PathVariable Long smokingAreaId) {
        ReviewStarsInfoResponse result = reviewService.getStarsInfoBySmokingAreaId(smokingAreaId);

        return ApiResponse.onSuccess(result);
    }
}
