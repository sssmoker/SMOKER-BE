package com.ssmoker.smoker.domain.review.dto;

import java.util.List;

public record ReviewStarsInfoResponse(List<Double> stars, double avg, int count) {
}
