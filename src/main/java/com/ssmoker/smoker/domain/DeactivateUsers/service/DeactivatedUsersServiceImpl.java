package com.ssmoker.smoker.domain.DeactivateUsers.service;

import com.ssmoker.smoker.domain.DeactivateUsers.domain.DeactivatedUsers;
import com.ssmoker.smoker.domain.DeactivateUsers.repository.DeactivatedUsersRepository;
import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.global.security.exception.AuthException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeactivatedUsersServiceImpl implements DeactivatedUsersService {
    private final DeactivatedUsersRepository deactivatedUsersRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void addToDeactivateTable(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND)); // 실제 존제하는지 확인
        DeactivatedUsers deactivatedUsers = DeactivatedUsers.builder()
                .inactiveUserId(member.getId())
                .build();
        deactivatedUsersRepository.save(deactivatedUsers);
    } //테이블에 유저 ID 추가
}
