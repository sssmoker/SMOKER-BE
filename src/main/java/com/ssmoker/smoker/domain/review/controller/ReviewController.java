package com.ssmoker.smoker.domain.review.controller;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.review.dto.ReviewRequestDTO;
import com.ssmoker.smoker.domain.review.service.ReviewService;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.security.handler.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "흡연 구역 리뷰 작성")
    @PostMapping("/{smokingAreaId}")
    public ApiResponse<Long> createReview(@PathVariable Long smokingAreaId,
                                          @RequestBody @Valid ReviewRequestDTO reviewRequestDTO,
                                          @Parameter(name = "user", hidden = true) @AuthUser Member member) {

        Long reviewId = reviewService.saveReview(smokingAreaId, reviewRequestDTO, member);
        return ApiResponse.onSuccess(reviewId);
    }
}
