package com.ssmoker.smoker.domain.smokingArea.dto;

import com.ssmoker.smoker.domain.smokingArea.domain.Feature;
import com.ssmoker.smoker.domain.smokingArea.domain.Location;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;

public record SmokingAreaInfoResponse(String smokingAreaName
        , Location location
        , Feature feature) {

    public static SmokingAreaInfoResponse of(final SmokingArea smokingArea) {
        return new SmokingAreaInfoResponse(
                smokingArea.getSmokingAreaName(),
                smokingArea.getLocation(),
                smokingArea.getFeature()
        );
    }
}
