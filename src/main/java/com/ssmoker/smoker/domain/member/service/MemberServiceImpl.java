package com.ssmoker.smoker.domain.member.service;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import com.ssmoker.smoker.global.exception.GeneralException;
import com.ssmoker.smoker.global.exception.SmokerBadRequestException;
import com.ssmoker.smoker.global.security.exception.AuthException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findMemberById(Long memberId) {
        log.info("memberId: {}", memberId);
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
    } // 유저 찾기

    @Override
    @Transactional
    public void updateNickname(Long memberId, String nickname) {
        Member member = findMemberById(memberId);
        if (member.getNickName().equals(nickname)) {
            return;
        }
        if (nickname == null || nickname.trim().isEmpty() || nickname.length() > 15) {
            throw new SmokerBadRequestException(ErrorStatus.FORBIDDEN_NICKNAME);
        }
        if(memberRepository.existsByNickName(nickname)) {
            throw new SmokerBadRequestException(ErrorStatus.DUPLICATE_NICKNAME);
        }
        member.setNickName(nickname);
    } // 닉네임 변경

}
