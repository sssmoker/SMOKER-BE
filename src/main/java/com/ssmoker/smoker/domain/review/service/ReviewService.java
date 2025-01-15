package com.ssmoker.smoker.domain.review.service;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.review.dto.ReviewRequestDTO;
import com.ssmoker.smoker.domain.review.repository.ReviewRepository;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import com.ssmoker.smoker.domain.review.exception.InvalidReviewException;
import com.ssmoker.smoker.domain.smokingArea.exception.SmokingAreaNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SmokingAreaRepository smokingAreaRepository;

    @Transactional
    public Long saveReview(Long smokingAreaId, ReviewRequestDTO reviewRequestDTO, Member member) {
        SmokingArea smokingArea = smokingAreaRepository.findById(smokingAreaId)
                .orElseThrow(() -> new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND));

        validateReviewData(reviewRequestDTO);

        Review review = new Review(
                null,
                smokingArea,
                reviewRequestDTO.getScore(),
                reviewRequestDTO.getContent(),
                reviewRequestDTO.getImageUrl(),
                member
        );

        Review savedReview = reviewRepository.save(review);
        return savedReview.getId();
    }

    private void validateReviewData(ReviewRequestDTO reviewRequestDTO) {
        if (reviewRequestDTO.getContent().length() > 1000) {
            throw new InvalidReviewException(REVIEW_CONTENT_EXCEED);
        }
    }
}
