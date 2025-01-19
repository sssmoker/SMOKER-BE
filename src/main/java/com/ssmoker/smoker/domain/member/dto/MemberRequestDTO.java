package com.ssmoker.smoker.domain.member.dto;

import lombok.Getter;

public class MemberRequestDTO {

    @Getter
    public static class updateNicknameRequestDTO {
        String nickname;
    }
}
