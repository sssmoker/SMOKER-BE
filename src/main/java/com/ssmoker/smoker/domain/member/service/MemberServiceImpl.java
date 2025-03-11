package com.ssmoker.smoker.domain.member.service;

import static com.ssmoker.smoker.domain.member.converter.MemberConverter.toMemberUpdateDTO;
import static com.ssmoker.smoker.global.exception.code.ErrorStatus.FORBIDDEN_NICKNAME;
import static com.ssmoker.smoker.global.exception.code.ErrorStatus.USER_NOT_FOUND;

import com.ssmoker.smoker.domain.member.converter.MemberConverter;
import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.dto.MemberRequestDTO;
import com.ssmoker.smoker.domain.member.dto.MemberResponseDTO;
import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.review.repository.ReviewRepository;
import com.ssmoker.smoker.domain.updatedHistory.domain.UpdatedHistory;
import com.ssmoker.smoker.domain.updatedHistory.repository.UpdatedHistoryRepository;
import com.ssmoker.smoker.global.aws.s3.AmazonS3Manager;
import com.ssmoker.smoker.global.exception.SmokerClientException;
import com.ssmoker.smoker.global.exception.SmokerServerError;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import java.util.List;
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
    private final UpdatedHistoryRepository updatedHistoryRepository;
    private final AmazonS3Manager amazonS3Manager;

    @Override
    public Member findMemberById(Long memberId) {
        log.info("memberId: {}", memberId);
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new SmokerClientException(USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updateNickname(Long memberId, String nickname) {
        Member member = findMemberById(memberId);
        if (member.getNickName().equals(nickname)) {
            return;
        }
        if (nickname == null || nickname.trim().isEmpty() || nickname.length() > 15) {
            throw new SmokerClientException(FORBIDDEN_NICKNAME);
        }
        member.updateNickName(nickname);
    }

    @Override
    @Transactional
    public String updateProfileImage(Long memberId, MemberRequestDTO.updateProfileImageRequestDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new SmokerClientException(USER_NOT_FOUND));
        try {
            String uuid = UUID.randomUUID().toString();
            String keyName = amazonS3Manager.generateProfileKeyName(uuid);
            String imageUrl = amazonS3Manager.uploadFile(keyName, request.getMultipartFile());
            member.updateImageUrl(imageUrl);
            memberRepository.save(member);
            return imageUrl;
        } catch (IOException e) {
            throw new SmokerServerError("파일 업로드 오류입니다.");
        }
    }

    @Override
    @Transactional
    public MemberResponseDTO.MemberProfileDTO viewProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new SmokerClientException(USER_NOT_FOUND));
        return new MemberResponseDTO.MemberProfileDTO(memberId, member.getNickName(), member.getProfileImageUrl());
    }

    @Override
    @Transactional
    public MemberResponseDTO.MemberReviewListDTO viewMemberReviews(Long memberId, Integer page) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new SmokerClientException(USER_NOT_FOUND));
        PageRequest pageRequest = PageRequest.of(page - 1, 5);

        Page<Review> reviewPage = reviewRepository.findAllByMember(member, pageRequest);
        MemberResponseDTO.MemberReviewListDTO memberReviewList = MemberConverter.toMemberReviewListDTO(reviewPage);

        return memberReviewList;
    }

    @Override
    @Transactional
    public MemberResponseDTO.MemberUpdateListDTO viewMemberUpdateHistory(Long memberId, Integer page) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new SmokerClientException(USER_NOT_FOUND));
        Page<UpdatedHistory> updatedHistoryPage = updatedHistoryRepository.findAllByMember(PageRequest.of(page - 1, 5),
                member);

        List<MemberResponseDTO.MemberUpdateDTO> responseDTOS = updatedHistoryPage.stream()
                .map(updatedHistory -> toMemberUpdateDTO(
                        updatedHistory,
                        updatedHistoryRepository.countBySmokingAreaId(updatedHistory.getSmokingArea().getId())))
                .toList();

        return MemberConverter.toMemberUpdateListDTO(responseDTOS, updatedHistoryPage.getTotalPages());
    }
}
