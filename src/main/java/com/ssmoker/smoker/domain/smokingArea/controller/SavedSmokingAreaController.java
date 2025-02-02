package com.ssmoker.smoker.domain.smokingArea.controller;

import com.ssmoker.smoker.domain.smokingArea.dto.MapResponse;
import com.ssmoker.smoker.domain.smokingArea.service.SmokingAreaService;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.security.handler.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/saved-smoking-area")
public class SavedSmokingAreaController {

    final SmokingAreaService smokingAreaService;

    @Operation(summary = "흡연구역 저장")
    @PostMapping("/{smokingAreaId}")
    public ApiResponse<?> saveSmokingArea(@AuthUser Long memberId, @PathVariable Long smokingAreaId) {
        smokingAreaService.createSavedSmokingArea(memberId, smokingAreaId);
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "저장된 흡연구역 삭제")
    @DeleteMapping("/{smokingAreaId}")
    public ApiResponse<?> deleteSavedSmokingArea(@AuthUser Long memberId, @PathVariable Long smokingAreaId) {
        smokingAreaService.deleteSavedSmokingArea(memberId, smokingAreaId);
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "저장된 흡연구역 목록 조회 및 검색")
    @GetMapping
    public ApiResponse<MapResponse.SmokingAreaListResponse> getSavedSmokingArea(@AuthUser Long memberId,
                                              @RequestParam(value = "userLat") Double userLat,
                                              @RequestParam(value = "userLng") Double userLng,
                                              @RequestParam(value = "filterBy", required = false, defaultValue = "address")
                                              @Parameter(description = "검색 필터",
                                                      examples =
                                                              {@ExampleObject(name = "주소명", value = "address"),
                                                              @ExampleObject(name = "장소명", value = "name")}) String filterBy,
                                              @RequestParam(value = "q", required = false)
                                              @Parameter(description = "검색어") String query) {
        return ApiResponse.onSuccess(new MapResponse.SmokingAreaListResponse(smokingAreaService.getSavedSmokingAreaList(memberId, userLat, userLng, filterBy, query)));
    }
}
