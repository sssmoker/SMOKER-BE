package com.ssmoker.smoker.domain.notice.service;

import com.ssmoker.smoker.domain.notice.dto.NoticeResponse;
import com.ssmoker.smoker.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeResponse.NoticeListResponse getNotices(Integer page) {
        //paging number는 1부터 되도록 하기
        //정렬은 최신순(desc)
        //size는 7
    }
}
