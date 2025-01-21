package com.ssmoker.smoker.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class MemberRequestDTO {

    @Getter
    public static class updateNicknameRequestDTO {
        String nickname;
    }

    @Getter
    @Setter
    public static class updateProfileImageRequestDTO {
        private MultipartFile multipartFile;
    }
}
