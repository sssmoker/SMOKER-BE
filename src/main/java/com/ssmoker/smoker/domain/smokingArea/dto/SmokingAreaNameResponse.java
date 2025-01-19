package com.ssmoker.smoker.domain.smokingArea.dto;

import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;

public record SmokingAreaNameResponse(
        String smokingAreaName
) {
    public static SmokingAreaNameResponse of(SmokingArea smokingArea) {
        return new SmokingAreaNameResponse(smokingArea.getSmokingAreaName());
    }
}

