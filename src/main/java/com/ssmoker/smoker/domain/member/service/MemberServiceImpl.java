package com.ssmoker.smoker.domain.member.service;

import com.ssmoker.smoker.domain.member.dto.AuthResponseDTO;
import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import com.ssmoker.smoker.global.exception.AuthException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.security.authDTO.KakaoProfile;
import com.ssmoker.smoker.security.authDTO.OAuthToken;
import com.ssmoker.smoker.security.converter.AuthConverter;
import com.ssmoker.smoker.security.provider.JwtTokenProvider;
import com.ssmoker.smoker.security.provider.KakaoAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoAuthProvider kakaoAuthProvider;

    @Override
    public Member findMemberById(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public AuthResponseDTO.OAuthResponse kakaoLogin(String code) {
        OAuthToken oAuthToken;
        try {
            oAuthToken = kakaoAuthProvider.requestToken(code);
        } catch (Exception e){
            throw new AuthException(ErrorStatus.AUTH_INVALID_CODE);
        }

        KakaoProfile kakaoProfile;
        try {
            kakaoProfile =
                    kakaoAuthProvider.requestKakaoProfile(oAuthToken.getAccess_token());
        }catch (Exception e){
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO);
        }

        // 유저 정보 받기
        Optional<Member> queryMember =
                memberRepository.findByEmail(
                        kakaoProfile.getKakaoAccount().getEmail());

        // 가입자 혹은 비가입자 체크해서 로그인 처리
        if (queryMember.isPresent()) {
            Member member = queryMember.get();
            String accessToken = jwtTokenProvider.createAccessToken(member.getId());
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
            member.updateToken(accessToken, refreshToken);
            memberRepository.save(member);
            return AuthConverter.toOAuthResponse(accessToken, refreshToken, member);
        } else {
            Member member = memberRepository.save(AuthConverter.toMember(kakaoProfile));
            String accessToken = jwtTokenProvider.createAccessToken(member.getId());
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
            member.updateToken(accessToken, refreshToken);
            memberRepository.save(member);
            return AuthConverter.toOAuthResponse(accessToken, refreshToken, member);
        }
    }

    @Override
    @Transactional
    public AuthResponseDTO.TokenRefreshResponse refresh(String refreshToken) {
        jwtTokenProvider.isTokenValid(refreshToken);
        Long id = jwtTokenProvider.getId(refreshToken);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
        String newAccessToken =
                jwtTokenProvider.createAccessToken(id);
        String newRefreshToken =
                jwtTokenProvider.createRefreshToken(id);
        member.updateToken(newAccessToken, newRefreshToken);
        memberRepository.save(member);
        // refreshTokenService.saveToken(newRefreshToken);
        return AuthConverter.toTokenRefreshResponse(newAccessToken, newRefreshToken);
    }


}
