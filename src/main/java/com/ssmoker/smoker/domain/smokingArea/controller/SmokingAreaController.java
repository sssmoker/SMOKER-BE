package com.ssmoker.smoker.domain.smokingArea.controller;

import com.ssmoker.smoker.domain.smokingArea.dto.*;
import com.ssmoker.smoker.domain.smokingArea.dto.MapResponse;
import com.ssmoker.smoker.domain.smokingArea.service.SmokingAreaService;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.apiPayload.code.SuccessStatus;
import com.ssmoker.smoker.global.security.handler.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

   //지도 api
    @Operation(summary = "흡연 구역 지도_마커표시 위함",
    description = "프론트가 지도에 마커표시를 할 수 있도록 흡연구역 db를 보내주는 역할")
    @GetMapping
    public ApiResponse<MapResponse.SmokingMarkersResponse> getSmokingAreaMakersInfo(){
        MapResponse.SmokingMarkersResponse markers = smokingAreaService.getSmokingMarkersResponse();

        return ApiResponse.of(SuccessStatus.MAP_INFO_OK, markers);
    }

    @Operation(summary = "흡연 구역 마커 클릭(모달)",
    description = "흡연구역 마커 클릭 시 다른 작은 창으로 마커 정보 알려주는 역할")
    @GetMapping("/{smokingAreaId}/simple")
    public ApiResponse<MapResponse.MarkerResponse> getSmokingAreaMarker(
            @PathVariable(name = "smokingAreaId") Long smokingAreaId,
            @RequestParam(name = "userLat") Double userLat,
            @RequestParam(name = "userLng") Double userLng
    ){
        MapResponse.MarkerResponse marker = smokingAreaService.getMarkerResponse(
                smokingAreaId, userLat, userLng);

        return ApiResponse.of(SuccessStatus.MAP_MARKER_OK,marker);
    }

    @Operation(summary = "흡연 구역 목록",
    description = "사용자 현재 위치에서 1km 반경 내에 있는 흡연 구역 목록 조회")
    @GetMapping("/list")
    public ApiResponse<MapResponse.SmokingAreaListResponse> getSmokingAreaList(
            @RequestParam(name = "userLat") Double userLat,
            @RequestParam(name = "userLng") Double userLng,
            @RequestParam(name = "filter") String filter
    ){
        MapResponse.SmokingAreaListResponse result
                = smokingAreaService.getSmokingAreaListResponse(userLat, userLng, filter);

        return ApiResponse.of(SuccessStatus.MAP_LIST_OK, result);
    }

    @Operation(summary = "상세정보 업데이트 화면")
    @GetMapping("/update/{smokingAreaId}/name")
    public ApiResponse<SmokingAreaNameResponse> getSmokingAreaName(@PathVariable Long smokingAreaId) {
        SmokingAreaNameResponse response = smokingAreaService.getSmokingAreaName(smokingAreaId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "흡연 구역 상세 정보 업데이트")
    @PatchMapping("/update/{smokingAreaId}")
    public ApiResponse<SmokingAreaUpdateRequest> updateSmokingArea(@PathVariable Long smokingAreaId,
                                                                   @RequestBody SmokingAreaUpdateRequest request,
                                                                   @AuthUser Long memberId) {
        SmokingAreaUpdateRequest response = smokingAreaService.updateSmokingArea(smokingAreaId, request, memberId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "흡연 구역 상세 정보 수정 완료, 수정 횟수 조회")
    @GetMapping("/update/complete/{smokingAreaId}")
    public ApiResponse<SmokingAreaDetailResponse> getSmokingAreaDetails(@PathVariable Long smokingAreaId) {
        SmokingAreaDetailResponse response = smokingAreaService.getSmokingAreaDetails(smokingAreaId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "흡연 구역 위치 정보 저장")
    @PostMapping("/register")
    public ApiResponse<Long> registerSmokingArea(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String address
    ) {
        SmokingAreaLocationRequest request = new SmokingAreaLocationRequest(latitude, longitude, address);
        Long smokingAreaId = smokingAreaService.registerSmokingArea(request);
        return ApiResponse.onSuccess(smokingAreaId);
    }
}
