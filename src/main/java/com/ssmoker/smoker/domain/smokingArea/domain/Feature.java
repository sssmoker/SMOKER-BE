package com.ssmoker.smoker.domain.smokingArea.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Feature {

    @Column(nullable = false)
    private Boolean hasAirPurifier; // 공기 청정 기능

    @Column(nullable = false)
    private Boolean hasAirConditioning; // 냉난방 기능

    @Column(nullable = false)
    private Boolean hasChair; // 의자 제공

    @Column(nullable = false)
    private Boolean hasTrashBin; // 쓰레기통

    @Column(nullable = false)
    private Boolean hasVentilationSystem; // 환기 시스템

    @Column(nullable = false)
    private Boolean isAccessible; // 휠체어 진입 가능 여부

    @Column(nullable = false)
    private Boolean hasCCTV; // CCTV 설치 여부

    @Column(nullable = false)
    private Boolean hasEmergencyButton; // 비상 버튼

    @Column(nullable = false)
    private Boolean hasVoiceGuidance; // 음성 안내 시스템

    @Column(nullable = false)
    private Boolean hasFireExtinguisher; // 소화기 비치 여부

    @Column(nullable = false)
    private Boolean isRegularlyCleaned; // 정기적인 청소 여부

    @Column(nullable = false)
    private Boolean hasCigaretteDisposal; // 담배꽁초 처리함 제공 여부

    @Column(nullable = false)
    private Boolean hasSunshade; // 햇빛 차단 시설

    @Column(nullable = false)
    private Boolean hasRainProtection; // 비바람 차단 시설


    public Feature update(Feature updatedFeature) {
        return new Feature(
                updatedFeature.hasAirPurifier,
                updatedFeature.hasAirConditioning,
                updatedFeature.hasChair,
                updatedFeature.hasTrashBin,
                updatedFeature.hasVentilationSystem,
                updatedFeature.isAccessible,
                updatedFeature.hasCCTV,
                updatedFeature.hasEmergencyButton,
                updatedFeature.hasVoiceGuidance,
                updatedFeature.hasFireExtinguisher,
                updatedFeature.isRegularlyCleaned,
                updatedFeature.hasCigaretteDisposal,
                updatedFeature.hasSunshade,
                updatedFeature.hasRainProtection
        );
    }

    public Feature(Boolean hasAirPurifier, Boolean hasAirConditioning, Boolean hasChair, Boolean hasTrashBin,
                   Boolean hasVentilationSystem, Boolean isAccessible, Boolean hasCCTV, Boolean hasEmergencyButton,
                   Boolean hasVoiceGuidance, Boolean hasFireExtinguisher, Boolean isRegularlyCleaned,
                   Boolean hasCigaretteDisposal, Boolean hasSunshade, Boolean hasRainProtection) {
        this.hasAirPurifier = hasAirPurifier != null ? hasAirPurifier : false;
        this.hasAirConditioning = hasAirConditioning != null ? hasAirConditioning : false;
        this.hasChair = hasChair != null ? hasChair : false;
        this.hasTrashBin = hasTrashBin != null ? hasTrashBin : false;
        this.hasVentilationSystem = hasVentilationSystem != null ? hasVentilationSystem : false;
        this.isAccessible = isAccessible != null ? isAccessible : false;
        this.hasCCTV = hasCCTV != null ? hasCCTV : false;
        this.hasEmergencyButton = hasEmergencyButton != null ? hasEmergencyButton : false;
        this.hasVoiceGuidance = hasVoiceGuidance != null ? hasVoiceGuidance : false;
        this.hasFireExtinguisher = hasFireExtinguisher != null ? hasFireExtinguisher : false;
        this.isRegularlyCleaned = isRegularlyCleaned != null ? isRegularlyCleaned : false;
        this.hasCigaretteDisposal = hasCigaretteDisposal != null ? hasCigaretteDisposal : false;
        this.hasSunshade = hasSunshade != null ? hasSunshade : false;
        this.hasRainProtection = hasRainProtection != null ? hasRainProtection : false;
    }

    public static Feature makeEmptyFeature() {
        return new Feature(false, false, false, false, false, false, false, false, false, false, false, false, false,
                false);
    }
}

