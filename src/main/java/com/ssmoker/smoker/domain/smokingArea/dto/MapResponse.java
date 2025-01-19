package com.ssmoker.smoker.domain.smokingArea.dto;

import com.ssmoker.smoker.domain.smokingArea.domain.Location;
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
        private final double distance;
        private final int reviewCount;
        private final int savedCount;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SmokingAreaInfoWithDistance{
        private final Long smokingAreaId;
        private final String smokingAreaName;
        private final Double distance;
        private final Location location;
        private final int reviewCount;
        private final int savedCount;
    }
}
