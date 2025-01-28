package com.ssmoker.smoker.domain.notice.service;

import com.ssmoker.smoker.domain.notice.domain.Notice;
import com.ssmoker.smoker.domain.notice.dto.NoticeResponse;
import com.ssmoker.smoker.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeResponse.NoticeListResponse getNotices(Integer page) {
        //paging number는 1부터 되도록 하기
        //정렬은 최신순(desc)
        //size는 7
        Pageable pageable
                = PageRequest.of(page, 7, Sort.by("updatedAt").descending());

        Page<Notice> notices
                = noticeRepository.findAll(pageable);

        List<NoticeResponse.NoticeViewResponse> noticeViewList
                = notices.stream().map(notice ->
                NoticeResponse.NoticeViewResponse());

    }
}
