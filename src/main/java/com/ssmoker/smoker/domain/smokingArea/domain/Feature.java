package com.ssmoker.smoker.domain.smokingArea.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Feature {

    @Column(nullable = false)
    private Boolean hasVentilationSystem; // 환풍 시설이 있어요

    @Column(nullable = false)
    private Boolean isClean; // 깔끔해요

    @Column(nullable = false)
    private Boolean hasTrashBin; // 쓰레기통이 있어요

    @Column(nullable = false)
    private Boolean hasChair; // 의자가 있어요

    @Column(nullable = false)
    private Boolean isAccessible; // 장애인 편의시설이에요

    @Column(nullable = false)
    private Boolean hasAirConditioning; // 냉난방이 가능해요

    @Column(nullable = false)
    private Boolean isOutdoor; // 야외에 있어요

}
