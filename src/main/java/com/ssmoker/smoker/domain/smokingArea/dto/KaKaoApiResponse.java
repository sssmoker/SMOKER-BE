package com.ssmoker.smoker.domain.smokingArea.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KaKaoApiResponse {
    @JsonProperty("documents")
    private List<KaKaoResponse> documents;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class KaKaoResponse {
        @JsonProperty("y") //위도
        private final Double latitude;

        @JsonProperty("x") //경도
        private final Double longitude;
    }
}
