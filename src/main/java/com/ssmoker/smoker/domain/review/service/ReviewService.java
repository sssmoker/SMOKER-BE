package com.ssmoker.smoker.domain.review.service;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.REVIEW_BAD_REQUEST;

import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.review.dto.ReviewStarsInfoResponse;
import com.ssmoker.smoker.domain.review.exception.ReviewPageNumberException;
import com.ssmoker.smoker.domain.review.repository.ReviewRepository;
import com.ssmoker.smoker.domain.review.dto.ReviewResponse;
import com.ssmoker.smoker.domain.review.dto.ReviewResponses;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 조회 관련 메서드 , READ WRITE 있고 트랜잭션 필요하면 메서드 위에 추가해주세요
public class ReviewService {

    private static final int REVIEW_PAGE_SIZE = 5;

    private final ReviewRepository reviewRepository;

    public ReviewResponses getReviewsBySmokingAreaId(Long id, int pageNumber) {
        if (pageNumber < 0) {
            throw new ReviewPageNumberException(REVIEW_BAD_REQUEST);
        }
        Page<Review> reviewPage = reviewRepository.findReviewsWithMemberBySmokingAreaId(id,
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

    public ReviewStarsInfoResponse getStarsInfoBySmokingAreaId(Long smokingAreaId) {
        return reviewRepository.findStarsInfoBySmokingAreaId(smokingAreaId);
    }
}
