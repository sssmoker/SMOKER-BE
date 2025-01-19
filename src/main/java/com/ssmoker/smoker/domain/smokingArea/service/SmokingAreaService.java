package com.ssmoker.smoker.domain.smokingArea.service;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.REVIEW_BAD_REQUEST;
import static com.ssmoker.smoker.global.exception.code.ErrorStatus.SMOKING_AREA_NOT_FOUND;

import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.review.repository.ReviewRepository;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.dto.*;
import com.ssmoker.smoker.domain.smokingArea.exception.ReviewPageNumberException;
import com.ssmoker.smoker.domain.smokingArea.exception.SmokingAreaNotFoundException;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SmokingAreaService {

    private static final int REVIEW_PAGE_SIZE = 5;

    private final SmokingAreaRepository smokingAreaRepository;
    private final ReviewRepository reviewRepository;

    public SmokingAreaInfoResponse getSmokingAreaInfo(Long id) {
        Optional<SmokingArea> smokingArea = smokingAreaRepository.findById(id);
        if (smokingArea.isPresent()) {
            return SmokingAreaInfoResponse.of(smokingArea.get());
        }
        throw new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND);
    }

    public ReviewResponses getReviews(Long id, int pageNumber) {
        Page<Review> reviewPage = reviewRepository.findReviewsWithMemberById(id,
                PageRequest.of(pageNumber, REVIEW_PAGE_SIZE));

        ReviewResponses reviewResponses = ReviewResponses.of(getReviewResponses(reviewPage), reviewPage.isLast(),
                reviewPage.getNumber());
        return reviewResponses;
    }

    private List<ReviewResponse> getReviewResponses(Page<Review> reviewPage) {
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
    private double calculateHaversineDistance(Double userLat, Double userLng,
                                     Double destLat,Double destLng) {
        double distance;
        double earthRad = 6371000;

        double latRad1 = Math.toRadians(userLat);
        double latRad2 = Math.toRadians(destLat);

        double deltaLat = Math.toRadians(destLat - userLat); //위도 차
        double deltaLng = Math.toRadians(destLng - userLng); //경도 차

        //공식 적용
        double haver = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(latRad1) * Math.cos(latRad2) * Math.pow(
                        Math.sin(deltaLng / 2), 2);

        double haversin = 2 * Math.atan2(Math.sqrt(haver),
                Math.sqrt(1 - haver));

        //단위 m
        double dis = earthRad * haversin;

        //도보 거리 보정
        //임의로 해둠
        double walkingFactor = 1.3;
        distance = dis * walkingFactor;

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

            //saved Count

        }

        return new  MapResponse.MarkerResponse(distance,reviewCount,savedCount);
    }
}
