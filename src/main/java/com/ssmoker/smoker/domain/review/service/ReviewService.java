package com.ssmoker.smoker.domain.review.service;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.REVIEW_BAD_REQUEST;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.exception.MemberNotFoundException;
import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.review.dto.*;
import com.ssmoker.smoker.domain.review.exception.ReviewPageNumberException;
import com.ssmoker.smoker.domain.review.exception.ReviewNotFoundException;
import com.ssmoker.smoker.domain.review.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import com.ssmoker.smoker.domain.smokingArea.exception.SmokingAreaNotFoundException;
import com.ssmoker.smoker.global.aws.s3.AmazonS3Manager;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 조회 관련 메서드 , READ WRITE 있고 트랜잭션 필요하면 메서드 위에 추가해주세요
public class ReviewService {

    private static final int REVIEW_PAGE_SIZE = 5;

    private final ReviewRepository reviewRepository;
    private final SmokingAreaRepository smokingAreaRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3Manager amazonS3Manager;

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
    public Long saveReview(Long smokingAreaId, MultipartFile img, ReviewRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        SmokingArea smokingArea = smokingAreaRepository.findById(smokingAreaId)
                .orElseThrow(() -> new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND));

        String imageUrl = uploadImageIfExists(img);

        Review newReview = Review.builder()
                .score(request.score())
                .content(request.content())
                .member(member)
                .imageUrl(imageUrl)
                .smokingArea(smokingArea)
                .build();

        reviewRepository.save(newReview);
        return newReview.getId();
    }

    private String uploadImageIfExists(MultipartFile img) {
        if (img == null || img.isEmpty()) {
            return null;
        }
        try {
            final String uuid = UUID.randomUUID().toString();
            final String keyName = amazonS3Manager.generateProfileKeyName(uuid);
            return amazonS3Manager.uploadFile(keyName, img);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 오류입니다.");
        }
    }

    public ReviewGetResponse getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(REVIEW_NOT_FOUND));

        SmokingArea smokingArea = smokingAreaRepository.findById(review.getSmokingArea().getId())
                .orElseThrow(() -> new SmokingAreaNotFoundException(SMOKING_AREA_NOT_FOUND));

        return ReviewGetResponse.of(review, smokingArea);
    }

}
