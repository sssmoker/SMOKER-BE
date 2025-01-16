package com.ssmoker.smoker.domain.member.service;

import com.ssmoker.smoker.domain.member.dto.AuthResponseDTO;
import com.ssmoker.smoker.domain.member.dto.MemberResponseDTO;
import com.ssmoker.smoker.domain.member.domain.Member;

public interface MemberService {
    Member findMemberById(Long memberId);

    AuthResponseDTO.OAuthResponse kakaoLogin(String code);

    AuthResponseDTO.TokenRefreshResponse refresh(String refreshToken);

    AuthResponseDTO.OAuthResponse GoogleLogin(String code);

    void deactivateMember(Long memberId);
}
