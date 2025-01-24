package com.ssmoker.smoker.domain.smokingArea.dto;

import com.ssmoker.smoker.domain.smokingArea.domain.Location;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;

public record SmokingAreaLocationRequest(
        Double latitude,
        Double longitude,
        String address
) {
    public static SmokingAreaLocationRequest of(SmokingArea smokingArea) {
        Location location = smokingArea.getLocation();
        return new SmokingAreaLocationRequest(
                location.getLatitude(),
                location.getLongitude(),
                location.getAddress()
        );
    }
}
