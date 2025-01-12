package com.ssmoker.smoker.domain.smokingArea.dto;

import com.ssmoker.smoker.domain.review.domain.Review;
import java.time.LocalDateTime;

public record ReviewResponse (double score
        , String content
        , String imageUrl
        , String memberName
        , LocalDateTime creationDate) {

    public static ReviewResponse of(final Review review,final String memberName) {
        return new ReviewResponse(
                review.getScore(),
                review.getContent(),
                review.getImageUrl(),
                memberName,
                review.getCreatedAt()
        );
    }
}
