package com.ssmoker.smoker.domain.smokingArea.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SmokingAreaRequest {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchRequest{
        private String search;
        private Double userLat;
        private Double userLng;
        private String filter;
    }
}
