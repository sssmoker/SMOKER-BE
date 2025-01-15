package com.ssmoker.smoker.domain.updatedHistory.controller;

import com.ssmoker.smoker.domain.updatedHistory.dto.UpdatedHistoryResponses;
import com.ssmoker.smoker.domain.updatedHistory.service.UpdatedHistoryService;
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
@RequestMapping("/api/updated-history")
public class UpdatedHistoryController {

    private final UpdatedHistoryService updatedHistoryService;

    @Operation(summary = "업데이트 내역 조회, 흡연 구역 기준", description = "쿼리 스트링으로 원하는 페이지를 넘겨주시면 됩니다(0Page 부터 시작).")
    @GetMapping("/{smokingAreaId}")
    public ApiResponse<UpdatedHistoryResponses> getUpdatedHistoryBySmokingArea(@PathVariable Long smokingAreaId,
                                                                  @RequestParam  @Min(0) @NotNull Integer page) {
        UpdatedHistoryResponses result = updatedHistoryService.getUpdatedHistoryBySmokingAreaId(smokingAreaId, page);

        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "업데이트 내역 조회, 멤버 기준", description = "쿼리 스트링으로 원하는 페이지를 넘겨주시면 됩니다(0Page 부터 시작).")
    public ApiResponse<UpdatedHistoryResponses> getUpdatedHistoryByMember(@PathVariable Long memberId,
                                                                  @RequestParam  @Min(0) @NotNull Integer page) {
        UpdatedHistoryResponses result = updatedHistoryService.getUpdatedHistoryByMemberId(memberId, page);

        return ApiResponse.onSuccess(result);
    }
}
