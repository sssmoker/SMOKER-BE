package com.ssmoker.smoker.global.security.controller;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.token.domain.Token;
import com.ssmoker.smoker.domain.token.repository.TokenRepository;
import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.global.security.authDTO.AuthResponseDTO;
import com.ssmoker.smoker.global.security.authDTO.AuthResponseDTO.OAuthResponse;
import com.ssmoker.smoker.global.security.authDTO.GoogleProfile;
import com.ssmoker.smoker.global.security.authDTO.KakaoProfile;
import com.ssmoker.smoker.global.security.authDTO.OAuthToken;
import com.ssmoker.smoker.global.security.converter.AuthConverter;
import com.ssmoker.smoker.global.security.exception.AuthException;
import com.ssmoker.smoker.global.security.provider.GoogleAuthProvider;
import com.ssmoker.smoker.global.security.provider.JwtTokenProvider;
import com.ssmoker.smoker.global.security.provider.KakaoAuthProvider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final KakaoAuthProvider kakaoAuthProvider;
    private final GoogleAuthProvider googleAuthProvider;
    private final JwtTokenProvider jwtTokenProvider;

    //카카오 로그인
    @Transactional
    public AuthResponseDTO.OAuthResponse kakaoLogin(String code) {
        OAuthToken oAuthToken = getKakaoOauthToken(code);

        KakaoProfile kakaoProfile;
        try {
            kakaoProfile =
                    kakaoAuthProvider.requestKakaoProfile(oAuthToken.getAccess_token());
        } catch (Exception e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_KAKAO);
        }

        // 유저 정보 받기
        Optional<Member> queryMember =
                memberRepository.findByEmail(
                        kakaoProfile.getKakaoAccount().getEmail());

        // 가입자 혹은 비가입자 체크해서 로그인 처리
        if (queryMember.isPresent()) {
            Member member = queryMember.get();
            return getOauthResponseForPresentUser(member, queryMember);
        }

        Member member = memberRepository.save(AuthConverter.kakaoToMember(kakaoProfile));
        return getOauthResponseForNewUser(member);
    }

    // 구글 로그인
    @Transactional
    public AuthResponseDTO.OAuthResponse GoogleLogin(String code) {
        OAuthToken oAuthToken = getGooleOauthToken(code);

        GoogleProfile googleProfile;
        try {
            googleProfile =
                    googleAuthProvider.requestGoogleProfile(oAuthToken.getAccess_token());
        } catch (Exception e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_GOOGLE);
        }

        // 유저 정보 받기
        Optional<Member> queryMember =
                memberRepository.findByEmail(
                        googleProfile.getEmail()); //이메일

        // 가입자 혹은 비가입자 체크해서 로그인 처리
        if (queryMember.isPresent()) {
            Member member = queryMember.get();
            return getOauthResponseForPresentUser(member, queryMember);
        }

        Member member = memberRepository.save(AuthConverter.googleToMember(googleProfile));
        return getOauthResponseForNewUser(member);
    }

    private OAuthResponse getOauthResponseForNewUser(Member member) {
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
        saveNewMember(refreshToken, member);
        return AuthConverter.toOAuthResponse(accessToken, refreshToken, member);
    }

    private OAuthResponse getOauthResponseForPresentUser(Member member, Optional<Member> queryMember) {
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
        Optional<Token> savedRefreshToken = tokenRepository.findByMemberId(member.getId());
        if (savedRefreshToken.isPresent()) {
            savedRefreshToken.get().changeToken(refreshToken);
        } else {
            tokenRepository.save(new Token(refreshToken, member.getId()));
        }
        return AuthConverter.toOAuthResponse(accessToken, refreshToken, queryMember.get());
    }

    private void saveNewMember(String refreshToken, Member member) {
        tokenRepository.save(new Token(refreshToken, member.getId()));
        memberRepository.save(member);
    }

    private OAuthToken getGooleOauthToken(String code) {
        OAuthToken oAuthToken;
        try {
            oAuthToken = googleAuthProvider.requestToken(code);
        } catch (Exception e) {
            throw new AuthException(ErrorStatus.AUTH_INVALID_CODE);
        }
        return oAuthToken;
    }

    private OAuthToken getKakaoOauthToken(String code) {
        OAuthToken oAuthToken;
        try {
            oAuthToken = kakaoAuthProvider.requestToken(code);
        } catch (Exception e) {
            throw new AuthException(ErrorStatus.AUTH_INVALID_CODE);
        }
        return oAuthToken;
    }
}
