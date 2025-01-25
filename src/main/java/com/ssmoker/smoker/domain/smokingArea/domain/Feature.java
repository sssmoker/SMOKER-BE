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

    private Boolean isEnclosedSmokingArea = false;

    private Boolean hasChair = false;

    private Boolean hasTrashBin = false;

    private Boolean hasAirConditioning = false;
}
