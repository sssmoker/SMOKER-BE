package com.ssmoker.smoker.domain.smokingArea.controller;

import com.ssmoker.smoker.domain.smokingArea.dto.*;
import com.ssmoker.smoker.domain.smokingArea.dto.MapResponse;
//import com.ssmoker.smoker.domain.smokingArea.dto.ReviewResponses;
import com.ssmoker.smoker.domain.smokingArea.dto.SmokingAreaRequest;
import com.ssmoker.smoker.domain.smokingArea.service.GoogleVisionOCRService;
import com.ssmoker.smoker.domain.smokingArea.service.SmokingAreaService;
import com.ssmoker.smoker.domain.smokingArea.dto.SmokingAreaInfoResponse;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.apiPayload.code.SuccessStatus;
import com.ssmoker.smoker.global.exception.GeneralException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.global.security.handler.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/smoking-area")
public class SmokingAreaController {

    private final SmokingAreaService smokingAreaService;
    private final GoogleVisionOCRService googleVisionOCRService;

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
    @GetMapping("/marker")
    public ApiResponse<MapResponse.SmokingMarkersResponse> getSmokingAreaMakersInfo() {
        MapResponse.SmokingMarkersResponse markers = smokingAreaService.getSmokingMarkersResponse();

        return ApiResponse.of(SuccessStatus.MAP_INFO_OK, markers);
    }

    @Operation(summary = "흡연 구역 마커 클릭(모달)",
            description = "흡연구역 마커 클릭 시 다른 작은 창으로 마커 정보 알려주는 역할")
    @GetMapping("/simple/{smokingAreaId}")
    public ApiResponse<MapResponse.MarkerResponse> getSmokingAreaMarker(
            @PathVariable(name = "smokingAreaId") Long smokingAreaId,
            @RequestParam(name = "userLat") Double userLat,
            @RequestParam(name = "userLng") Double userLng
    ) {
        MapResponse.MarkerResponse marker = smokingAreaService.getMarkerResponse(
                smokingAreaId, userLat, userLng);

        return ApiResponse.of(SuccessStatus.MAP_MARKER_OK, marker);
    }

    @Operation(summary = "흡연 구역 목록",
            description = "사용자 현재 위치에서 1km 반경 내에 있는 흡연 구역 목록 조회")
    @GetMapping("/list")
    public ApiResponse<MapResponse.SmokingAreaListResponse> getSmokingAreaList(
            @RequestParam(name = "userLat") Double userLat,
            @RequestParam(name = "userLng") Double userLng,
            @RequestParam(name = "filter") String filter
    ) {
        MapResponse.SmokingAreaListResponse result
                = smokingAreaService.getSmokingAreaListResponse(userLat, userLng, filter);

        return ApiResponse.of(SuccessStatus.MAP_LIST_OK, result);
    }

    @Operation(summary = "흡연 구역 검색 목록",
            description = "검색어와 현재 유저의 위치를 request로 받고 흡연 구역 목록 조회")
    @PostMapping("/search")
    public ApiResponse<MapResponse.SmokingAreaListResponse> getSearchAreaList(
            @RequestBody SmokingAreaRequest.SearchRequest request) {
        MapResponse.SmokingAreaListResponse result
                = smokingAreaService.getSearchingAreaListResponse(request);

        return ApiResponse.of(SuccessStatus.MAP_SEARCH_OK, result);
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

    @Operation(summary = "구글 OCR 검증 및 S3 이미지 저장")
    @PostMapping(value = "/verify", consumes = "multipart/form-data")
    public ApiResponse<?> verifySmokingAreaOCR(
            @Parameter(description = "업로드할 이미지 파일", required = true)
            @RequestParam(value = "file") MultipartFile file) throws IOException {
        if(googleVisionOCRService.isSmokingArea(googleVisionOCRService.detectText(file))) {
            return ApiResponse.of(SuccessStatus.OCR_VERIFY_OK, googleVisionOCRService.uploadSmokingAreaImage(file));
        } else {
          throw new GeneralException(ErrorStatus.SMOKING_KEYWORD_NOT_FOUND);
        }
    }

    @Operation(summary = "검증된 새로운 흡연 구역 등록")
    @PostMapping("/register")
    public ApiResponse<Long> registerSmokingArea(
            @RequestParam String image_url,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String address,
            @RequestBody SmokingAreaRegisterRequest request) {

        Long smokingAreaId = smokingAreaService.saveSmokingArea(request, image_url, latitude, longitude, address);
        return ApiResponse.onSuccess(smokingAreaId);
    }
}
