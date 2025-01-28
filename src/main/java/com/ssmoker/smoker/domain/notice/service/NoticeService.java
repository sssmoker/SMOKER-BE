package com.ssmoker.smoker.domain.notice.service;

import com.ssmoker.smoker.domain.notice.domain.Notice;
import com.ssmoker.smoker.domain.notice.dto.NoticeResponse;
import com.ssmoker.smoker.domain.notice.exception.NoticeNotFoundException;
import com.ssmoker.smoker.domain.notice.repository.NoticeRepository;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeResponse.NoticeListResponse getNotices(Integer page) {
        //paging number는 1부터 되도록 하기
        //정렬은 최신순(desc)
        //size는 7
        if(page == null || page < 1) {
            throw new NoticeNotFoundException(ErrorStatus.NOTICE_BAD_REQUEST);
        }

        Pageable pageable
                = PageRequest.of(page - 1, 7, Sort.by("updatedAt").descending());

        Page<Notice> notices
                = noticeRepository.findAll(pageable);

        List<NoticeResponse.NoticeViewResponse> noticeViewList
                = notices.stream().map(notice
                -> new NoticeResponse.NoticeViewResponse(notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getUpdatedAt()))
                .collect(Collectors.toList());

        return new NoticeResponse.NoticeListResponse(
                noticeViewList,
                notices.getSize(),
                notices.getTotalPages(),
                notices.getTotalElements(),
                notices.isFirst(),
                notices.isLast()
        );

    }
}
