package com.ssmoker.smoker.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemberResponseDTO {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberProfileDTO{
        private Long memberId;
        private String nickName;
        private String ProfileImageUrl;
    }
}
