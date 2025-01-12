package com.ssmoker.smoker.domain.smokingArea.service.dto;

import com.ssmoker.smoker.domain.smokingArea.domain.Feature;
import com.ssmoker.smoker.domain.smokingArea.domain.Location;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;

public record SmokingAreaInfoResponse(String smokingAreaName
        , Location location
        , Feature feature) {
    public static SmokingAreaInfoResponse of(SmokingArea smokingArea) {

        return new SmokingAreaInfoResponse(
                smokingArea.getSmokingAreaName(),
                smokingArea.getLocation(),
                smokingArea.getFeature()
        );
    }
}
