package com.ssmoker.smoker.domain.smokingArea.dto;

import com.ssmoker.smoker.domain.smokingArea.domain.Feature;
import com.ssmoker.smoker.domain.smokingArea.domain.Location;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;

public record SmokingAreaRegisterRequest(
        String smokingAreaName,
        Feature feature,
        String areaType
) {
    public static SmokingArea of(SmokingAreaRegisterRequest request, String imageUrl, Double latitude, Double longitude, String address) {
        return SmokingArea.builder()
                .smokingAreaName(request.smokingAreaName())
                .location(new Location(address, latitude, longitude))
                .feature(request.feature())
                .imageUrl(imageUrl)
                .areaType(request.areaType())
                .build();
    }
}
