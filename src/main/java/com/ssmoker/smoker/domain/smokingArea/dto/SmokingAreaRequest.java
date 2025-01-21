package com.ssmoker.smoker.domain.smokingArea.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class SmokingAreaRequest {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class SearchRequest{
        private final String location;
        private final Double userLat;
        private final Double userLng;
        private final String filter;
    }
}
