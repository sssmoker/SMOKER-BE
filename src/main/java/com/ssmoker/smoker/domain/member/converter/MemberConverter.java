package com.ssmoker.smoker.domain.member.converter;

import com.ssmoker.smoker.domain.member.dto.MemberResponseDTO;
import com.ssmoker.smoker.domain.review.domain.Review;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class MemberConverter {

    public static MemberResponseDTO.MemberReviewDTO toMemberReviewDTO(Review review) {
        return MemberResponseDTO.MemberReviewDTO.builder()
                .reviewId(review.getId())
                .reviewScore(review.getScore())
                .createdAt(review.getCreatedAt())
                .imgUrl(review.getImageUrl())
                .content(review.getContent())
                .smokingArea((review.getSmokingArea()).getSmokingAreaName())
                .build();
    }

    public static MemberResponseDTO.MemberReviewListDTO toMemberReviewListDTO(Page<Review> reviews) {
        List<MemberResponseDTO.MemberReviewDTO> memberReviewDTOList = reviews.stream()
                .map(MemberConverter::toMemberReviewDTO).collect(Collectors.toList());
        Collections.reverse(memberReviewDTOList);
        return MemberResponseDTO.MemberReviewListDTO.builder()
                .MemberReviewList(memberReviewDTOList)
                .totalPage(reviews.getTotalPages())
                .build();
    }
}
