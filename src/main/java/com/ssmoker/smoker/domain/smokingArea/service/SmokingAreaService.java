package com.ssmoker.smoker.domain.smokingArea.service;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.REVIEW_BAD_REQUEST;
import static com.ssmoker.smoker.global.exception.code.ErrorStatus.SMOKING_AREA_NOT_FOUND;

import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.review.repository.ReviewRepository;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.dto.ReviewResponse;
import com.ssmoker.smoker.domain.smokingArea.dto.ReviewResponses;
import com.ssmoker.smoker.domain.smokingArea.exception.ReviewPageNumberException;
import com.ssmoker.smoker.domain.smokingArea.exception.SmokingAreaNotFoundException;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import com.ssmoker.smoker.domain.smokingArea.dto.SmokingAreaInfoResponse;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
}
