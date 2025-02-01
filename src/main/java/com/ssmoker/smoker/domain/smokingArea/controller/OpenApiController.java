package com.ssmoker.smoker.domain.smokingArea.controller;


import com.ssmoker.smoker.domain.smokingArea.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/open-api")
public class OpenApiController {

    private final OpenApiService openApiService;

    @GetMapping("/test")
    public String test() {
        try {
            openApiService.getPublicData();
            return "저장 완료";
        } catch (Exception e) {
            log.error("API 호출 실패", e);
            return "API 호출 실패: " + e.getMessage();
        }
    }
}
