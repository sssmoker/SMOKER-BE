package com.ssmoker.smoker.domain.smokingArea.service;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.SMOKING_AREA_NOT_FOUND;

import com.ssmoker.smoker.domain.review.repository.ReviewRepository;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.dto.*;
import com.ssmoker.smoker.domain.smokingArea.exception.SmokingAreaNotFoundException;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import com.ssmoker.smoker.global.exception.SmokerBadRequestException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmokingAreaService {

    private final SmokingAreaRepository smokingAreaRepository;
    private final ReviewRepository reviewRepository;

    public SmokingAreaInfoResponse getSmokingAreaInfo(Long id) {
        Optional<SmokingArea> smokingArea = smokingAreaRepository.findById(id);
        if (smokingArea.isPresent()) {
            return SmokingAreaInfoResponse.of(smokingArea.get());
        }
        throw new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND);
    }


    //marker를 위한 모든 db 보내기
    public MapResponse.SmokingMarkersResponse getSmokingMarkersResponse() {
        List<SmokingArea> smokingAreas = smokingAreaRepository.findAll();

        List<SmokingAreaMarkersResponse> markers =
                smokingAreas.stream()
                        .map(marker -> new SmokingAreaMarkersResponse(
                                marker.getId(),
                                marker.getSmokingAreaName(),
                                marker.getLocation()
                        ))
                        .collect(Collectors.toList());

        return new MapResponse.SmokingMarkersResponse(markers);
    }

    //거리 계산하는 함수
    //latitude = 위도, longitude = 경도
    private Double calculateHaversineDistance(Double userLat, Double userLng,
                                     Double destLat,Double destLng) {
        Double distance;
        double earthRad = 6371000;

        Double latRad1 = Math.toRadians(userLat);
        Double latRad2 = Math.toRadians(destLat);

        Double deltaLat = Math.toRadians(destLat - userLat); //위도 차
        Double deltaLng = Math.toRadians(destLng - userLng); //경도 차

        //공식 적용
        Double haver = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(latRad1) * Math.cos(latRad2) * Math.pow(
                        Math.sin(deltaLng / 2), 2);

        Double haversin = 2 * Math.atan2(Math.sqrt(haver),
                Math.sqrt(1 - haver));

        //단위 m
        Double dis = earthRad * haversin;

        //도보 거리 보정
        //임의로 해둠
        double walkingFactor = 1.3;
        distance = dis * walkingFactor;

        //소수점 한자리까지
        distance = Math.round(distance * 10)/10.0;

        return distance;
    }

    //marker 간단한 정보 보여주기 (모달)
    public MapResponse.MarkerResponse getMarkerResponse(Long smokingAreaId,
                                                        Double userLat,
                                                        Double userLng) {
        SmokingArea smokingArea = smokingAreaRepository.findById(smokingAreaId)
                .orElse(null);

        double distance;
        int reviewCount;
        int savedCount;

        //smoking area 예외처리
        if(smokingArea == null){
            throw new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND);
        }else{
            //거리 계산
            distance = calculateHaversineDistance(userLat, userLng,
                    smokingArea.getLocation().getLatitude(), smokingArea.getLocation().getLongitude());

            //review Count
            reviewCount = smokingAreaRepository.findReviewCountBySmokingAreaId(smokingAreaId);

            //saved Count
            savedCount = smokingAreaRepository.findSavedCountBySmokingAreaId(smokingAreaId);
        }

        return new  MapResponse.MarkerResponse(distance,reviewCount,savedCount);
    }

    private List<MapResponse.SmokingAreaInfoWithDistance> getSmokingAreaInfoWithDistance(
            Double userLat, Double userLng, String filter){
        //모든 Db 불러오기
        List<SmokingArea> smokingAreas =
                smokingAreaRepository.findBySmokingAreaIdWithin1km(userLat, userLng);

        //해당 db에 대한 모든 reviewCount와 savedCount 불러오기
        return smokingAreas.stream().map(smokingArea -> {
            Double distance = calculateHaversineDistance(userLat, userLng,
                    smokingArea.getLocation().getLatitude(),
                    smokingArea.getLocation().getLongitude());

            int reviewCount = smokingAreaRepository.findReviewCountBySmokingAreaId(
                    smokingArea.getId());

            int savedCount = smokingAreaRepository.findSavedCountBySmokingAreaId(
                    smokingArea.getId());

            Double avgRating = reviewRepository.findAvgScoreBySmokingId(smokingArea.getId());

            return new MapResponse.SmokingAreaInfoWithDistance(smokingArea.getId(),
                    smokingArea.getSmokingAreaName(),
                    distance,
                    smokingArea.getLocation(),
                    avgRating,
                    reviewCount,
                    savedCount
            );
        }).sorted((a,b) -> {
            if("nearest".equals(filter)){
                return Double.compare(a.getDistance(), b.getDistance()); //가까운 순
            }else if("highestRated".equals(filter)){
                return Comparator.comparing(MapResponse.SmokingAreaInfoWithDistance::getRating,
                                Comparator.reverseOrder())//별점 높은 순
                        .thenComparing(MapResponse.SmokingAreaInfoWithDistance::getDistance) //같으면 가까운 순
                        .compare(a,b);
            }else{
                throw new SmokerBadRequestException(ErrorStatus.FILTER_NOT_FOUND);
            }
        }).collect(Collectors.toList());
    }

    //db를 뒤져서 그에 맞는 smokingArea 구하기
    public MapResponse.SmokingAreaListResponse getSmokingAreaListResponse(
            Double userLat, Double userLng, String filter
    ) {
        List<MapResponse.SmokingAreaInfoWithDistance> smokingLists =
                getSmokingAreaInfoWithDistance(userLat, userLng, filter);

        return new MapResponse.SmokingAreaListResponse(smokingLists);
    }

    public SmokingAreaUpdateRequest updateSmokingArea(Long smokingAreaId, SmokingAreaUpdateRequest request) {
        SmokingArea smokingArea = smokingAreaRepository.findById(smokingAreaId)
                .orElseThrow(() -> new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND));

        smokingArea.getFeature().setHasAirConditioning(request.hasAirConditioning());
        smokingArea.getFeature().setHasChair(request.hasChair());
        smokingArea.getFeature().setHasTrashBin(request.hasTrashBin());
        smokingArea.getFeature().setIsEnclosedSmokingArea(request.isEnclosedSmokingArea());

        smokingArea = smokingAreaRepository.save(smokingArea);

        return SmokingAreaUpdateRequest.of(smokingArea);
    }

    public SmokingAreaNameResponse getSmokingAreaName(Long smokingAreaId) {
        SmokingArea smokingArea = smokingAreaRepository.findById(smokingAreaId)
                .orElseThrow(() -> new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND));

        return SmokingAreaNameResponse.of(smokingArea);
    }

}

