package com.ssmoker.smoker.domain.smokingArea.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class KaKaoApiResponse {
    @Getter
    @Setter
    public static class KaKaoResponse {
        @JsonProperty("y") //위도
        private Double latitude;

        @JsonProperty("x") //경도
        private Double longitude;
    }
}
