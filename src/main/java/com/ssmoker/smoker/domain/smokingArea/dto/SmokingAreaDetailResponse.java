package com.ssmoker.smoker.domain.smokingArea.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor  // ✅ 모든 필드 값을 받는 생성자 자동 생성
public class SmokingAreaDetailResponse {
    private int updateCount;
    private String smokingAreaName;
    private String address;
    private String imageUrl;
    private Boolean hasAirConditioning;
    private Boolean hasChair;
    private Boolean hasTrashBin;
    private Boolean isEnclosedSmokingArea;
}
