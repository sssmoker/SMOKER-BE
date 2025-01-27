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
    private Boolean isEnclosedSmokingArea;

    @Column(nullable = false)
    private Boolean hasChair;

    @Column(nullable = false)
    private Boolean hasTrashBin;

    @Column(nullable = false)
    private Boolean hasAirConditioning;

}
