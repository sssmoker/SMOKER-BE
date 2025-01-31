package com.ssmoker.smoker.domain.notice.controller;

import com.ssmoker.smoker.domain.notice.dto.NoticeResponse;
import com.ssmoker.smoker.domain.notice.repository.NoticeRepository;
import com.ssmoker.smoker.domain.notice.service.NoticeService;
import com.ssmoker.smoker.global.apiPayload.ApiResponse;
import com.ssmoker.smoker.global.apiPayload.code.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/notices")
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(summary = "공지사항 리스트 조회")
    @GetMapping
    public ApiResponse<NoticeResponse.NoticeListResponse> getNotices
            (@RequestParam(name = "page") Integer page){
        NoticeResponse.NoticeListResponse result
                = noticeService.getNotices(page);

        return ApiResponse.of(SuccessStatus.NOTICES_OK, result);
    }

    @Operation(summary = "공지사항 세부사항 조회")
    @GetMapping("/detail/{noticeId}")
    public ApiResponse<NoticeResponse.NoticeDetailResponse> getNotice
            (@PathVariable(name = "noticeId") Long noticeId){
        NoticeResponse.NoticeDetailResponse result
                = noticeService.getNotice(noticeId);

        return ApiResponse.of(SuccessStatus.NOTICE_DETAIL_OK, result);
    }
}
