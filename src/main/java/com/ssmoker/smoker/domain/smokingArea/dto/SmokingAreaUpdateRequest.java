package com.ssmoker.smoker.domain.smokingArea.dto;

import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;

public record SmokingAreaUpdateRequest(
        Boolean hasVentilationSystem,
        Boolean isClean,
        Boolean hasTrashBin,
        Boolean hasChair,
        Boolean isAccessible,
        Boolean hasAirConditioning,
        Boolean isOutdoor
) {
    public static SmokingAreaUpdateRequest of(SmokingArea smokingArea) {
        return new SmokingAreaUpdateRequest(
                smokingArea.getFeature().getHasVentilationSystem(),
                smokingArea.getFeature().getIsClean(),
                smokingArea.getFeature().getHasTrashBin(),
                smokingArea.getFeature().getHasChair(),
                smokingArea.getFeature().getIsAccessible(),
                smokingArea.getFeature().getHasAirConditioning(),
                smokingArea.getFeature().getIsOutdoor()
        );
    }
}
