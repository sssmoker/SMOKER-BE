package com.ssmoker.smoker.domain.smokingArea.dto;

import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;

public record SmokingAreaUpdateRequest(
        Boolean hasAirConditioning,
        Boolean hasChair,
        Boolean hasTrashBin,
        Boolean isEnclosedSmokingArea
) {
    public static SmokingAreaUpdateRequest of(SmokingArea smokingArea) {
        return new SmokingAreaUpdateRequest(
                smokingArea.getFeature().getHasAirConditioning(),
                smokingArea.getFeature().getHasChair(),
                smokingArea.getFeature().getHasTrashBin(),
                smokingArea.getFeature().getIsEnclosedSmokingArea()
        );
    }
}
