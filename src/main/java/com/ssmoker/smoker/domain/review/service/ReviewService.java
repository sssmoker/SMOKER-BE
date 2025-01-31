package com.ssmoker.smoker.domain.review.service;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.REVIEW_BAD_REQUEST;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.review.dto.ReviewStarsInfoResponse;
import com.ssmoker.smoker.domain.review.exception.ReviewPageNumberException;
import com.ssmoker.smoker.domain.review.dto.ReviewGetResponse;
import com.ssmoker.smoker.domain.review.dto.ReviewRequest;
import com.ssmoker.smoker.domain.review.exception.ReviewNotFoundException;
import com.ssmoker.smoker.domain.review.repository.ReviewRepository;
import com.ssmoker.smoker.domain.review.dto.ReviewResponse;
import com.ssmoker.smoker.domain.review.dto.ReviewResponses;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import com.ssmoker.smoker.domain.smokingArea.exception.SmokingAreaNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 조회 관련 메서드 , READ WRITE 있고 트랜잭션 필요하면 메서드 위에 추가해주세요
public class ReviewService {

    private static final int REVIEW_PAGE_SIZE = 5;

    private final ReviewRepository reviewRepository;
    private final SmokingAreaRepository smokingAreaRepository;

    public ReviewResponses getReviewsBySmokingAreaId(Long id, int pageNumber) {
        if (pageNumber < 0) {
            throw new ReviewPageNumberException(REVIEW_BAD_REQUEST);
        }
        Page<Review> reviewPage = reviewRepository.findReviewsWithMemberBySmokingAreaId(id,
                PageRequest.of(pageNumber, REVIEW_PAGE_SIZE));

        ReviewResponses reviewResponses = ReviewResponses.of(getReviewResponsesByAreaId(reviewPage),
                reviewPage.isLast(),
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

    public ReviewStarsInfoResponse getStarsInfoBySmokingAreaId(Long smokingAreaId) {
        List<Double> scores = reviewRepository.findScoresBySmokingAreaId(smokingAreaId);
        double avg = scores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        int size = scores.size();
        List<Integer> counts = getCountsGroupByStars(scores);

        return new ReviewStarsInfoResponse(counts, avg, size);
    }

    private static List<Integer> getCountsGroupByStars(List<Double> scores) {
        // 점수별 개수 세기 (내림 처리)
        Map<Double, Long> scoreCounts = scores.stream()
                .map(score -> Math.floor(score))  // 내림하여 정수 부분만 가져오기
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 1점부터 5점까지의 점수에 대해 개수 값을 0으로 채우기
        List<Integer> counts = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            counts.add(scoreCounts.getOrDefault((double) i, 0L).intValue());
        }

        return counts;
    }

    @Transactional
    public Long saveReview(Long smokingAreaId, ReviewRequest reviewRequest, Member member) {
        SmokingArea smokingArea = smokingAreaRepository.findById(smokingAreaId)
                .orElseThrow(() -> new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND));

        Review review = Review.builder()
                .smokingArea(smokingArea)
                .score(reviewRequest.score())
                .content(reviewRequest.content())
                .imageUrl(reviewRequest.imageUrl())
                .member(member).build();

        Review savedReview = reviewRepository.save(review);
        return savedReview.getId();
    }

    public ReviewGetResponse getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(REVIEW_NOT_FOUND));

        SmokingArea smokingArea = smokingAreaRepository.findById(review.getSmokingArea().getId())
                .orElseThrow(() -> new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND));

        return ReviewGetResponse.of(review, smokingArea);
    }

}
