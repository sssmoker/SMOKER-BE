package com.ssmoker.smoker.domain.member.service;

import com.ssmoker.smoker.domain.member.domain.Member;

public interface MemberService {
    Member findMemberById(Long memberId);
    void updateNickname(Long memberId, String nickname);
}
