package com.ssmoker.smoker.domain.member.converter;

import com.ssmoker.smoker.domain.member.dto.MemberResponseDTO;
import com.ssmoker.smoker.domain.review.domain.Review;
import com.ssmoker.smoker.domain.updatedHistory.domain.Action;
import com.ssmoker.smoker.domain.updatedHistory.domain.UpdatedHistory;
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

    public static MemberResponseDTO.MemberUpdateDTO toMemberUpdateDTO(UpdatedHistory updatedHistory) {
        String content = updatedHistory.getUpdateCount().toString();

        Action action = updatedHistory.getAction();
        switch (action) {
            case UPDATE:
                content += "번째 수정에 참여했어요!";
                break;
            case REGISTRATION:
                content = "장소를 등록했어요!";
                break;
        }
        return MemberResponseDTO.MemberUpdateDTO.builder()
                .updateHistoryId(updatedHistory.getId())
                .smokingAreaName(updatedHistory.getSmokingArea().getSmokingAreaName())
                .createdAt(updatedHistory.getCreatedAt())
                .content(content)
                .build();
    }

    public static MemberResponseDTO.MemberUpdateListDTO toMemberUpdateListDTO(Page<UpdatedHistory> updatedHistories) {
        List<MemberResponseDTO.MemberUpdateDTO> memberUpdateDTOList = updatedHistories.stream()
                .map(MemberConverter::toMemberUpdateDTO).collect(Collectors.toList());

        Collections.reverse(memberUpdateDTOList);

        return MemberResponseDTO.MemberUpdateListDTO.builder()
                .MemberUpdateList(memberUpdateDTOList)
                .totalPage(updatedHistories.getTotalPages())
                .build();
    }

}
