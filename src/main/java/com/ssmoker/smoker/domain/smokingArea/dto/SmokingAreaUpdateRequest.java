package com.ssmoker.smoker.domain.smokingArea.dto;

import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;

public record SmokingAreaUpdateRequest(
        Boolean hasAirPurifier,
        Boolean hasAirConditioning,
        Boolean hasChair,
        Boolean hasTrashBin,
        Boolean hasVentilationSystem,
        Boolean isAccessible,
        Boolean hasCCTV,
        Boolean hasEmergencyButton,
        Boolean hasVoiceGuidance,
        Boolean hasFireExtinguisher,
        Boolean isRegularlyCleaned,
        Boolean hasCigaretteDisposal,
        Boolean hasSunshade,
        Boolean hasRainProtection
) {
    public static SmokingAreaUpdateRequest of(SmokingArea smokingArea) {
        return new SmokingAreaUpdateRequest(
                smokingArea.getFeature().getHasAirPurifier(),
                smokingArea.getFeature().getHasAirConditioning(),
                smokingArea.getFeature().getHasChair(),
                smokingArea.getFeature().getHasTrashBin(),
                smokingArea.getFeature().getHasVentilationSystem(),
                smokingArea.getFeature().getIsAccessible(),
                smokingArea.getFeature().getHasCCTV(),
                smokingArea.getFeature().getHasEmergencyButton(),
                smokingArea.getFeature().getHasVoiceGuidance(),
                smokingArea.getFeature().getHasFireExtinguisher(),
                smokingArea.getFeature().getIsRegularlyCleaned(),
                smokingArea.getFeature().getHasCigaretteDisposal(),
                smokingArea.getFeature().getHasSunshade(),
                smokingArea.getFeature().getHasRainProtection()
        );
    }
}
