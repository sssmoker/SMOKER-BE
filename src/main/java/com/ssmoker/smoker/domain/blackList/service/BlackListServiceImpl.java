package com.ssmoker.smoker.domain.blackList.service;

import com.ssmoker.smoker.domain.blackList.domain.BlackList;
import com.ssmoker.smoker.domain.blackList.repository.BlackListRepository;
import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.global.security.exception.AuthException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListServiceImpl implements BlackListService {
    private final BlackListRepository blackListRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void addToBlackList(long memberId,String accessToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));

        BlackList blackList = BlackList.builder()
                .accessToken(accessToken)
                .build();
        blackListRepository.save(blackList);
    }
}
