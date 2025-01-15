package com.ssmoker.smoker.domain.smokingArea.controller;

import com.ssmoker.smoker.domain.smokingArea.dto.MapResponse;
import com.ssmoker.smoker.domain.smokingArea.dto.ReviewResponses;
import com.ssmoker.smoker.domain.smokingArea.dto.SmokingAreaMarkersResponse;
import com.ssmoker.smoker.domain.smokingArea.service.SmokingAreaService;
import com.ssmoker.smoker.domain.smokingArea.dto.SmokingAreaInfoResponse;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.apiPayload.code.SuccessStatus;
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
    public ApiResponse<ReviewResponses> getReviews(@PathVariable Long smokingAreaId,
                                                   @RequestParam @Min(0) @NotNull Integer pageNumber) {
        ReviewResponses result = smokingAreaService.getReviews(smokingAreaId, pageNumber);

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

/*    @Operation(summary = "흡연 구역 마커 클릭(모달)",
    description = "흡연구역 마커 클릭 시 다른 작은 창으로 마커 정보 알려주는 역할")
    @GetMapping("/{smokingAreaId}")
    public ApiResponse<MapResponse.MarkerResponse> getSmokingAreaMarker(
            @PathVariable(name = "smokingAreaId") Long smokingAreaId,
            @RequestParam Double userLat,
            @RequestParam Double userLng
    ){

    }*/
}
