package com.ssmoker.smoker.domain.token.service;

import static com.ssmoker.smoker.global.exception.code.ErrorStatus.NOT_CONTAIN_TOKEN;

import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import com.ssmoker.smoker.global.security.authDTO.AuthResponseDTO.TokenResponse;
import com.ssmoker.smoker.domain.token.domain.Token;
import com.ssmoker.smoker.domain.token.repository.TokenRepository;
import com.ssmoker.smoker.global.security.converter.AuthConverter;
import com.ssmoker.smoker.global.security.exception.AuthException;
import com.ssmoker.smoker.global.security.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    public TokenResponse reissueToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.extractToken(request);
        Token token = getToken(refreshToken);

        Long memberId = validateRefreshToken(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(memberId);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(memberId);

        // refreshToken 업데이트
        token.changeToken(newRefreshToken);
        return AuthConverter.toTokenRefreshResponse(newAccessToken, newRefreshToken);
    }

    public void deleteToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.extractToken(request);
        Token token = getToken(refreshToken);

        tokenRepository.delete(token);
    }

    private Token getToken(String refreshToken) {
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        if (!token.isPresent()) {
            throw new AuthException(NOT_CONTAIN_TOKEN);
            // Logout 되어있는 상황
        }
        return token.get();
    }


    private Long validateRefreshToken(String refreshToken) {
        jwtTokenProvider.isTokenValid(refreshToken);
        Long memberId = jwtTokenProvider.getId(refreshToken);
        return memberId;
    }

    public void deleteUser(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.extractToken(request);
        long memberId = validateRefreshToken(refreshToken);
        memberRepository.deleteById(memberId);
        deleteToken(request);
    }
}
