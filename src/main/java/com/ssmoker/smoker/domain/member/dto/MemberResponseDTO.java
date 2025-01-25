package com.ssmoker.smoker.domain.member.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class MemberResponseDTO {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberProfileDTO {
        private Long memberId;
        private String nickName;
        private String ProfileImageUrl;
    }

    @Builder
    @Getter
    public static class MemberReviewDTO{
        private Long reviewId;
        private LocalDateTime createdAt;
        private String smokingArea;
        private Double reviewScore;
        private String imgUrl;
        private String content;
    }

    @Builder
    @Getter
    public static class MemberReviewListDTO{
        private List<MemberReviewDTO> MemberReviewList;
        private Integer totalPage;
    }
}
