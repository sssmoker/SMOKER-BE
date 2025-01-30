package com.ssmoker.smoker.domain.smokingArea.service;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.MEMBER_NOT_FOUND;
import static com.ssmoker.smoker.global.exception.code.ErrorStatus.SMOKING_AREA_NOT_FOUND;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.review.repository.ReviewRepository;
import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import com.ssmoker.smoker.domain.smokingArea.domain.Feature;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.dto.*;
import com.ssmoker.smoker.domain.smokingArea.exception.SmokingAreaNotFoundException;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import com.ssmoker.smoker.domain.updatedHistory.domain.Action;
import com.ssmoker.smoker.domain.updatedHistory.domain.UpdatedHistory;
import com.ssmoker.smoker.domain.updatedHistory.repository.UpdatedHistoryRepository;
import com.ssmoker.smoker.global.exception.SmokerBadRequestException;
import com.ssmoker.smoker.global.exception.SmokerNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SmokingAreaService {

    private final SmokingAreaRepository smokingAreaRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final UpdatedHistoryRepository updatedHistoryRepository;
    private final KaKaoApiService kakaoApiService;

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

    //distance, avgReview, reviewCont, saveCount 계산 함수
    private MapResponse.SmokingAreaInfoWithRequest getDtoElement
    (Double userLat, Double userLng, SmokingArea smokingArea){
        Double distance = calculateHaversineDistance(userLat, userLng,
                smokingArea.getLocation().getLatitude(),
                smokingArea.getLocation().getLongitude());

        Double avgRating
                = reviewRepository.findAvgScoreBySmokingId(smokingArea.getId());

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

    public SmokingAreaUpdateRequest updateSmokingArea(Long smokingAreaId, SmokingAreaUpdateRequest request, Long memberId) { //상세정보 업데이트
        SmokingArea smokingArea = smokingAreaRepository.findById(smokingAreaId)
                .orElseThrow(() -> new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND));

        Feature updatedFeature = getFeature(request);
        smokingArea.updateFeature(updatedFeature);

        smokingArea = smokingAreaRepository.save(smokingArea);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new SmokerNotFoundException(MEMBER_NOT_FOUND));
        member.setUpdateCount(member.getUpdateCount() + 1);

        memberRepository.save(member);

        int updateCount = updatedHistoryRepository.countBySmokingAreaId(smokingAreaId)+1;
        Action action = Action.UPDATE;

        UpdatedHistory history = new UpdatedHistory(updateCount,action,member, smokingArea);
        updatedHistoryRepository.save(history);

        return SmokingAreaUpdateRequest.of(smokingArea);

    }

    public SmokingAreaNameResponse getSmokingAreaName(Long smokingAreaId) {
        SmokingArea smokingArea = smokingAreaRepository.findById(smokingAreaId)
                .orElseThrow(() -> new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND));

        return SmokingAreaNameResponse.of(smokingArea);
    }

    @Transactional(readOnly = true)
    public SmokingAreaDetailResponse getSmokingAreaDetails(Long smokingAreaId) {

        int updateCount = updatedHistoryRepository.countBySmokingAreaId(smokingAreaId);

        SmokingArea smokingArea = smokingAreaRepository.findById(smokingAreaId)
                .orElseThrow(() -> new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND));

        return new SmokingAreaDetailResponse(
                updateCount,
                smokingArea.getSmokingAreaName(),
                smokingArea.getLocation().getAddress(),
                smokingArea.getImageUrl(),
                smokingArea.getAreaType(),
                smokingArea.getFeature()
                );
    }

    private Feature getFeature(SmokingAreaUpdateRequest request) {
        return new Feature(
                request.hasAirPurifier(),        // 공기 청정 기능
                request.hasAirConditioning(),    // 냉난방 기능
                request.hasChair(),              // 의자 제공
                request.hasTrashBin(),           // 쓰레기통
                request.hasVentilationSystem(),  // 환기 시스템
                request.isAccessible(),          // 휠체어 진입 가능 여부
                request.hasCCTV(),               // CCTV 설치 여부
                request.hasEmergencyButton(),    // 비상 버튼
                request.hasVoiceGuidance(),      // 음성 안내 시스템
                request.hasFireExtinguisher(),   // 소화기 비치 여부
                request.isRegularlyCleaned(),    // 정기적인 청소 여부
                request.hasCigaretteDisposal(),  // 담배꽁초 처리함 제공 여부
                request.hasSunshade(),           // 햇빛 차단 시설
                request.hasRainProtection()      // 비바람 차단 시설
        );
    }

}

