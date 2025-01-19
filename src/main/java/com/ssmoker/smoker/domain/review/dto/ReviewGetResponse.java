package com.ssmoker.smoker.domain.review.dto;

import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;

public record ReviewGetResponse(
        Long id,
        double score,
        String content,
        String imageUrl,
        Long smokingAreaId,
        String smokingAreaName,
        String smokingAreaAddress
) {
    public static ReviewGetResponse of(final Review review, final SmokingArea smokingArea) {
        return new ReviewGetResponse(
                review.getId(),
                review.getScore(),
                review.getContent(),
                review.getImageUrl(),
                smokingArea.getId(),
                smokingArea.getSmokingAreaName(),
                smokingArea.getLocation().getAddress()
        );
    }
}

