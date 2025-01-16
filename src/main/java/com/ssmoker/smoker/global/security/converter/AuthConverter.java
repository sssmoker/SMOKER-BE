package com.ssmoker.smoker.global.security.converter;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.domain.MemberStatus;
import com.ssmoker.smoker.domain.member.dto.AuthResponseDTO;
import com.ssmoker.smoker.global.security.authDTO.GoogleProfile;
import com.ssmoker.smoker.global.security.authDTO.KakaoProfile;

public class AuthConverter {

    public static Member kakaoToMember(KakaoProfile kakaoProfile) {
        return Member.builder()
                .nickName(kakaoProfile.getKakaoNickname().getNickname())
                .email(kakaoProfile.getKakaoAccount().getEmail())
                .status(MemberStatus.ACTIVE)
                .build();
    }

    public static Member googleToMember(GoogleProfile googleProfile) {
        return Member.builder()
                .nickName(googleProfile.getNickName())
                .email(googleProfile.getEmail())
                .status(MemberStatus.ACTIVE)
                .build();
    }

    public static AuthResponseDTO.OAuthResponse toOAuthResponse(
            String accessToken, String refreshToken, Member member) {
        return AuthResponseDTO.OAuthResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .memberId(member.getId())
                .build();
    }

    public static AuthResponseDTO.TokenRefreshResponse toTokenRefreshResponse(
            String accessToken, String refreshToken) {
        return AuthResponseDTO.TokenRefreshResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
