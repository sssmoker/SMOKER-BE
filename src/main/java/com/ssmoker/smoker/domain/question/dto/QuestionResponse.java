package com.ssmoker.smoker.domain.question.dto;

import com.ssmoker.smoker.domain.notice.dto.NoticeResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionResponse {

    @Getter
    @Builder
    public static class QuestionListResponse{
        List<QuestionResponse.QuestionViewResponse> questionList;
        Integer size;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Getter
    @Builder
    public static class QuestionViewResponse{
        public Long questionId;
        public String title;
        public LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class QuestionDetailResponse{
        public Long questionId;
        public String title;
        public String content;
        public LocalDateTime updatedAt;
    }

}
