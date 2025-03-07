package com.ssmoker.smoker.domain.smokingArea.controller;

import com.ssmoker.smoker.domain.smokingArea.service.OpenApiService;
import com.ssmoker.smoker.global.exception.SmokerServerError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/open-api")
public class OpenApiController {

    private final OpenApiService openApiService;

    @Value("${open-api.check-key}")
    private String check_key;

    @GetMapping("/{key}")
    public String getPublicData(@PathVariable String key) {
        if (check_key.equals(key)) {
            try {
                openApiService.getPublicData();
                return "저장 완료";
            } catch (Exception e) {
                throw new SmokerServerError("API 호출 실패");
            }
        }
        throw new SmokerServerError("키 오류");
    }
}