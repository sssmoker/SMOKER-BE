package com.ssmoker.smoker.domain.member.service;

import com.ssmoker.smoker.domain.member.domain.MemberStatus;
import com.ssmoker.smoker.domain.member.dto.AuthResponseDTO;
import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import com.ssmoker.smoker.global.security.exception.AuthException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.global.security.authDTO.GoogleProfile;
import com.ssmoker.smoker.global.security.authDTO.KakaoProfile;
import com.ssmoker.smoker.global.security.authDTO.OAuthToken;
import com.ssmoker.smoker.global.security.converter.AuthConverter;
import com.ssmoker.smoker.global.security.provider.GoogleAuthProvider;
import com.ssmoker.smoker.global.security.provider.JwtTokenProvider;
import com.ssmoker.smoker.global.security.provider.KakaoAuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoAuthProvider kakaoAuthProvider;
    private final GoogleAuthProvider googleAuthProvider;

    @Override
    public Member findMemberById(Long memberId) {
        log.info("memberId: {}", memberId);
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
    } // 유저 찾기

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
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_KAKAO);
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
            memberRepository.save(member);
            return AuthConverter.toOAuthResponse(accessToken, refreshToken, member);
        } else {
            Member member = memberRepository.save(AuthConverter.kakaoToMember(kakaoProfile));
            String accessToken = jwtTokenProvider.createAccessToken(member.getId());
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
            memberRepository.save(member);
            return AuthConverter.toOAuthResponse(accessToken, refreshToken, member);
        }
    } //카카오 로그인

    @Override
    @Transactional
    public AuthResponseDTO.OAuthResponse GoogleLogin(String code) {
        OAuthToken oAuthToken;
        try {
            oAuthToken = googleAuthProvider.requestToken(code);
        } catch (Exception e){
            throw new AuthException(ErrorStatus.AUTH_INVALID_CODE);
        }

        GoogleProfile googleProfile;
        try {
            googleProfile =
                    googleAuthProvider.requestGoogleProfile(oAuthToken.getAccess_token());
        }catch (Exception e){
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_GOOGLE);
        }

        // 유저 정보 받기
        Optional<Member> queryMember =
                memberRepository.findByEmail(
                        googleProfile.getEmail()); //이메일


        if (queryMember.isPresent()) {
            Member member = queryMember.get();
            String accessToken = jwtTokenProvider.createAccessToken(member.getId());
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
            memberRepository.save(member);
            return AuthConverter.toOAuthResponse(accessToken, refreshToken, member);
        } else {
            Member member = memberRepository.save(AuthConverter.googleToMember(googleProfile));
            String accessToken = jwtTokenProvider.createAccessToken(member.getId());
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
            memberRepository.save(member);
            return AuthConverter.toOAuthResponse(accessToken, refreshToken, member);
        }
    } // 구글 로그인

    @Override
    @Transactional
    public void deactivateMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
        memberRepository.delete(member);
    } // 회원 탈퇴

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
        return AuthConverter.toTokenRefreshResponse(newAccessToken, newRefreshToken);
    } // 토큰 재발급

}
