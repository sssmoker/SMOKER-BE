package com.ssmoker.smoker.domain.smokingArea.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class KaKaoApiResponse {

    @JsonProperty("documents")
    private List<KaKaoResponse> documents;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class KaKaoResponse {
        @JsonProperty("y") //위도
        private Double latitude;

        @JsonProperty("x") //경도
        private Double longitude;
    }
}
