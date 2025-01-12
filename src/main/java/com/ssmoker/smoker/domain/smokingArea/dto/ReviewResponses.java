package com.ssmoker.smoker.domain.smokingArea.dto;

import java.util.List;

public record ReviewResponses(List<ReviewResponse> reviews,
                              boolean isLastPage,
                              int pageNumber) {

    public static ReviewResponses of(List<ReviewResponse> reviews, boolean isLastPage, int pageNumber) {
        return new ReviewResponses(reviews, isLastPage, pageNumber);
    }
}
