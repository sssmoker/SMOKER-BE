package com.ssmoker.smoker.domain.smokingArea.controller;

import com.ssmoker.smoker.domain.smokingArea.dto.ReviewResponses;
import com.ssmoker.smoker.domain.smokingArea.service.SmokingAreaService;
import com.ssmoker.smoker.domain.smokingArea.dto.SmokingAreaInfoResponse;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/smoking-area")
public class SmokingAreaController {

    private final SmokingAreaService smokingAreaService;

    //Swagger 를 어디까지 적어야할 지
    @Operation(summary = "흡연 구역 상세 조회")
    @GetMapping("/{smokingAreaId}")
    public ApiResponse<SmokingAreaInfoResponse> getSmokingArea(@PathVariable Long smokingAreaId) {
        SmokingAreaInfoResponse result = smokingAreaService.getSmokingAreaInfo(smokingAreaId);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "흡연 구역 리뷰 조회(최신순)", description = "쿼리 스트링으로 원하는 페이지를 넘겨주시면 됩니다.")
    @GetMapping("/{smokingAreaId}/reviews")
    public ApiResponse<ReviewResponses> getReviews(@PathVariable Long smokingAreaId, @RequestParam int pageNumber) {
        ReviewResponses result = smokingAreaService.getReviews(smokingAreaId, pageNumber);

        return ApiResponse.onSuccess(result);
    }
}
