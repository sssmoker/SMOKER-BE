package com.ssmoker.smoker.domain.member.service;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.dto.MemberRequestDTO;
import com.ssmoker.smoker.domain.member.dto.MemberResponseDTO;

public interface MemberService {
    Member findMemberById(Long memberId);
    void updateNickname(Long memberId, String nickname);
    String updateProfileImage(Long memberId, MemberRequestDTO.updateProfileImageRequestDTO request);
    MemberResponseDTO.MemberProfileDTO viewProfile(Long memberId);
    MemberResponseDTO.MemberReviewListDTO viewMemberReviews(Long memberId, Integer page);
    MemberResponseDTO.MemberUpdateListDTO viewMemberUpdateHistory(Long memberId, Integer page);
}
