package com.ssmoker.smoker.global.security.converter;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.domain.MemberStatus;
import com.ssmoker.smoker.global.security.authDTO.AuthResponseDTO;
import com.ssmoker.smoker.global.security.authDTO.AuthResponseDTO.TokenResponse;
import com.ssmoker.smoker.global.security.authDTO.GoogleProfile;
import com.ssmoker.smoker.global.security.authDTO.KakaoProfile;

public class AuthConverter {
    private static final String DEFAULT_PROFILE_IMG = "https://smoker-bucket.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%ED%95%84%EC%9D%B4%EB%AF%B8%EC%A7%80.png";

    public static Member kakaoToMember(KakaoProfile kakaoProfile) {
        return Member.builder()
                .nickName(kakaoProfile.getKakaoNickname().getNickname())
                .email(kakaoProfile.getKakaoAccount().getEmail())
                .updateCount(0)
                .profileImageUrl(DEFAULT_PROFILE_IMG)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    public static Member googleToMember(GoogleProfile googleProfile) {
        return Member.builder()
                .nickName(googleProfile.getNickName())
                .email(googleProfile.getEmail())
                .updateCount(0)
                .profileImageUrl(DEFAULT_PROFILE_IMG)
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

    public static TokenResponse toTokenRefreshResponse(
            String accessToken, String refreshToken) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
