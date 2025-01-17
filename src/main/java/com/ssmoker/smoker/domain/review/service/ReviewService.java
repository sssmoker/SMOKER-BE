package com.ssmoker.smoker.domain.review.service;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.review.dto.ReviewRequest;
import com.ssmoker.smoker.domain.review.repository.ReviewRepository;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
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
    public Long saveReview(Long smokingAreaId, ReviewRequest reviewRequest, Member member) {
        SmokingArea smokingArea = smokingAreaRepository.findById(smokingAreaId)
                .orElseThrow(() -> new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND));

        System.out.println("member:" + member.getId()+ " " + member.getNickName()+ " " + member.getEmail());

        Review review = Review.builder()
                .smokingArea(smokingArea)
                .score(reviewRequest.score())
                .content(reviewRequest.content())
                .imageUrl(reviewRequest.imageUrl())
                .member(member).build();

        Review savedReview = reviewRepository.save(review);
        return savedReview.getId();
    }
}
