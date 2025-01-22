package com.ssmoker.smoker.domain.member.service;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.dto.MemberRequestDTO;
import com.ssmoker.smoker.domain.member.dto.MemberResponseDTO;
import com.ssmoker.smoker.test.TestRequestDto;

public interface MemberService {
    Member findMemberById(Long memberId);
    void updateNickname(Long memberId, String nickname);
    String updateProfileImage(Long memberId, MemberRequestDTO.updateProfileImageRequestDTO request);
    MemberResponseDTO.MemberProfileDTO viewProfile(Long memberId);
}
