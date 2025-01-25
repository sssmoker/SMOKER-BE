package com.ssmoker.smoker.domain.smokingArea.service;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.REVIEW_BAD_REQUEST;
import static com.ssmoker.smoker.global.exception.code.ErrorStatus.SMOKING_AREA_NOT_FOUND;
import static com.ssmoker.smoker.global.exception.code.ErrorStatus._BAD_REQUEST;

import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.review.exception.ReviewPageNumberException;
import com.ssmoker.smoker.domain.review.repository.ReviewRepository;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.dto.*;
import com.ssmoker.smoker.domain.smokingArea.exception.SmokingAreaNotFoundException;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.exception.SmokerBadRequestException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmokingAreaService {

    private static final int REVIEW_PAGE_SIZE = 5;

    private final SmokingAreaRepository smokingAreaRepository;
    private final ReviewRepository reviewRepository;
    private final KaKaoApiService kakaoApiService;

    public SmokingAreaInfoResponse getSmokingAreaInfo(Long id) {
        Optional<SmokingArea> smokingArea = smokingAreaRepository.findById(id);
        if (smokingArea.isPresent()) {
            return SmokingAreaInfoResponse.of(smokingArea.get());
        }
        throw new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND);
    }

    public ReviewResponses getReviewsByAreaId(Long id, int pageNumber) {
        if (pageNumber < 0) {
            throw new ReviewPageNumberException(REVIEW_BAD_REQUEST);
        }
        Page<Review> reviewPage = reviewRepository.findReviewsWithMemberById(id,
                PageRequest.of(pageNumber, REVIEW_PAGE_SIZE));

        ReviewResponses reviewResponses = ReviewResponses.of(getReviewResponsesByAreaId(reviewPage), reviewPage.isLast(),
                reviewPage.getNumber());
        return reviewResponses;
    }

    private List<ReviewResponse> getReviewResponsesByAreaId(Page<Review> reviewPage) {
        return reviewPage.getContent().stream()
                .map(this::getReviewResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse getReviewResponse(Review review) {
        return ReviewResponse.of(review, review.getMember().getNickName());
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

    //distance, avgReview, reviewCont, saveCount 계산 함수
    private MapResponse.SmokingAreaInfoWithRequest getDtoElement
    (Double userLat, Double userLng, SmokingArea smokingArea){
        Double distance = calculateHaversineDistance(userLat, userLng,
                smokingArea.getLocation().getLatitude(),
                smokingArea.getLocation().getLongitude());

        Double avgRating
                = reviewRepository.findAvgScore(smokingArea.getId());

        int reviewCount
                = smokingAreaRepository.findReviewCountBySmokingAreaId(
                smokingArea.getId());

        int savedCount
                = smokingAreaRepository.findSavedCountBySmokingAreaId(
                smokingArea.getId());

        return new MapResponse.SmokingAreaInfoWithRequest(
                smokingArea.getId(),
                smokingArea.getSmokingAreaName(),
                distance,
                smokingArea.getLocation(),
                avgRating,
                reviewCount,
                savedCount
        );
    }

    //정렬(filter)
    private Comparator<MapResponse.SmokingAreaInfoWithRequest> sorting(String filter){
        if("nearest".equals(filter)){
            return Comparator.comparing(MapResponse.SmokingAreaInfoWithRequest::getDistance); //가까운 순
        } else if("highestRated".equals(filter)){
            return Comparator.comparing(MapResponse.SmokingAreaInfoWithRequest::getRating,
                            Comparator.reverseOrder())//별점 높은 순
                    .thenComparing(MapResponse.SmokingAreaInfoWithRequest::getDistance); //같으면 가까운 순
        }else{
            throw new SmokerBadRequestException(ErrorStatus.FILTER_NOT_FOUND);
        }
    }

    //정렬 코드는 개선 가능함. 나중에 시간이 되면 개선해야겠음
    private List<MapResponse.SmokingAreaInfoWithRequest> getSmokingAreaInfoWithDistance(
            Double userLat, Double userLng, String filter){
        //모든 Db 불러오기
        List<SmokingArea> smokingAreas =
                smokingAreaRepository.findBySmokingAreaIdWithin1km(userLat, userLng);

        //해당 db에 대한 모든 reviewCount와 savedCount 불러오기
        return smokingAreas.stream().map(
                smokingArea -> getDtoElement(userLat,userLng,smokingArea)
                ).sorted(sorting(filter))
                .collect(Collectors.toList());
    }

    //db를 뒤져서 그에 맞는 smokingArea 구하기
    public MapResponse.SmokingAreaListResponse getSmokingAreaListResponse(
            Double userLat, Double userLng, String filter
    ) {
        List<MapResponse.SmokingAreaInfoWithRequest> smokingLists =
                getSmokingAreaInfoWithDistance(userLat, userLng, filter);

        return new MapResponse.SmokingAreaListResponse(smokingLists);
    }

    //db로 검색어 찾기
    private List<MapResponse.SmokingAreaInfoWithRequest> getSmokingAreaWithSearching(
            SmokingAreaRequest.SearchRequest searchRequest
    ){
        //카카오 api 를 통해 키워드의 중심 좌표 찾기
        KaKaoApiResponse.KaKaoResponse center
                = kakaoApiService.getCenterLocationFromKakao
                (searchRequest.getSearch());

        //검색어로 찾기
        List<SmokingArea> smokingAreas
                = smokingAreaRepository.findBySearch(
                        searchRequest.getSearch(),
                center.getLatitude(),
                center.getLongitude());

        //SmokingAreaInfoWithRequest dto에 넣기
        return smokingAreas.stream().map(smokingArea ->
           getDtoElement(searchRequest.getUserLat(), searchRequest.getUserLng(),
                   smokingArea)
        ).sorted(sorting(searchRequest.getFilter()))
        .collect(Collectors.toList());
    }

    public MapResponse.SmokingAreaListResponse getSearchingAreaListResponse
            (SmokingAreaRequest.SearchRequest searchRequest) {
        List<MapResponse.SmokingAreaInfoWithRequest> smokingLists =
                getSmokingAreaWithSearching(searchRequest);

        return new MapResponse.SmokingAreaListResponse(smokingLists);
    }
}
