package com.ssmoker.smoker.global.security.filter;

import com.ssmoker.smoker.domain.blackList.repository.BlackListRepository;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.global.security.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckAccessTokenBlacklist {

    private final BlackListRepository blackListRepository;

    public void checkBlackList (String accessToken) throws AuthException {
        boolean existInBlackList = blackListRepository.existsByAccessToken(accessToken);
        if (existInBlackList) {
            throw new AuthException(ErrorStatus.AUTH_INVALID_TOKEN);
        }
    }
}
