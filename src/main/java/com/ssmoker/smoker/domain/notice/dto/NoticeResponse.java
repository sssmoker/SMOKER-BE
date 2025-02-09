package com.ssmoker.smoker.domain.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class NoticeResponse {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeListResponse{
        List<NoticeViewResponse> notices;
        Integer size;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeViewResponse{
        public Long noticeId;
        public String title;
        public LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeDetailResponse{
        public Long noticeId;
        public String title;
        public String content;
        public LocalDateTime updatedAt;
    }
}
