package com.ssmoker.smoker.domain.member.dto;

import lombok.*;

public class MemberResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyPageResponse {
        Long memberId;
        String nickname;
        String email;
        Float categories;
    }
}
