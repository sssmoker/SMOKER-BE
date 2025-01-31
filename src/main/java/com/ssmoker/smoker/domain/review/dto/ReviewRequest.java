package com.ssmoker.smoker.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRequest(
        @NotNull(message = "점수는 필수입니다.")
        Double score,

        @NotBlank(message = "내용은 필수입니다.")
        @Size(max = 1000, message = "내용은 최대 1000자까지 입력 가능합니다.")
        String content ){}
