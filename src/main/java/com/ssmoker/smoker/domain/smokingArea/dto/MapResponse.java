package com.ssmoker.smoker.domain.smokingArea.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MapResponse {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SmokingMarkersResponse{
        private final List<SmokingAreaMarkersResponse> makers;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class MarkerResponse{
        private final Double distance;
        private final int reviewCount;
        private final int savedCount;
    }
}
