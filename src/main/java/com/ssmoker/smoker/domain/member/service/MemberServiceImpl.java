package com.ssmoker.smoker.domain.member.service;

import com.ssmoker.smoker.domain.member.converter.MemberConverter;
import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.dto.MemberRequestDTO;
import com.ssmoker.smoker.domain.member.dto.MemberResponseDTO;
import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.review.repository.ReviewRepository;
import com.ssmoker.smoker.global.aws.s3.AmazonS3Manager;
import com.ssmoker.smoker.global.exception.GeneralException;
import com.ssmoker.smoker.global.exception.SmokerBadRequestException;
import com.ssmoker.smoker.global.security.exception.AuthException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final AmazonS3Manager amazonS3Manager;

    @Override
    public Member findMemberById(Long memberId) {
        log.info("memberId: {}", memberId);
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
    }

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
    }

    @Override
    @Transactional
    public String updateProfileImage(Long memberId ,MemberRequestDTO.updateProfileImageRequestDTO request){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        try{
            final String uuid = UUID.randomUUID().toString();
            final String keyName = amazonS3Manager.generateProfileKeyName(uuid);
            final String imageUrl = amazonS3Manager.uploadFile(keyName, request.getMultipartFile());
            member.setProfileImageUrl(imageUrl);
            memberRepository.save(member);
            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 오류입니다.");
        }
    }

    @Override
    @Transactional
    public MemberResponseDTO.MemberProfileDTO viewProfile(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        return new MemberResponseDTO.MemberProfileDTO(memberId,member.getNickName(),member.getProfileImageUrl());
    }

    @Override
    @Transactional
    public MemberResponseDTO.MemberReviewListDTO viewMemberReviews(Long memberId, Integer page){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        PageRequest pageRequest = PageRequest.of(page - 1, 5);

        Page<Review> reviewPage = reviewRepository.findAllByMember(member,pageRequest);
        MemberResponseDTO.MemberReviewListDTO memberReviewList = MemberConverter.toMemberReviewListDTO(reviewPage);

        return memberReviewList;
    }
}
