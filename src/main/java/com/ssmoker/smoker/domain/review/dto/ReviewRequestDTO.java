package com.ssmoker.smoker.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {

    private Long smokingAreaId;

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long memberId;

    @NotNull(message = "점수는 필수입니다.")
    private Double score;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 1000, message = "내용은 최대 1000자까지 입력 가능합니다.")
    private String content;

    private String imageUrl;
}

