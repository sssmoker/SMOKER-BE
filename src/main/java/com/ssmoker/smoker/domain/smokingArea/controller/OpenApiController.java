package com.ssmoker.smoker.domain.smokingArea.controller;

import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.URI;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/open-api")
public class OpenApiController {

    private final RestTemplate restTemplate;

    // 디코딩된 API 키 사용 (포털에서 제공된 Decoding Key)
    private final static String API_KEY = "디코딩키";
    private final static String BASE_URL = "https://api.odcloud.kr/api/15090343/v1/uddi:7f5d9c71-fdc4-4a83-8c60-fa980eb70465";

    @GetMapping("/test")
    public String test() throws MalformedURLException {
        // 이미 인코딩된 키
        String preEncodedKey = "인코딩키";

        String apiUrl = BASE_URL +
                "?page=1" +
                "&perPage=10&serviceKey="+
                preEncodedKey;
        log.info("API 요청 URL: {}", apiUrl);

        try {
            // 헤더 설정 (필요 시 추가)
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, "application/json");

            // HttpEntity 생성
            HttpEntity<?> entity = new HttpEntity<>(headers);

            // RestTemplate을 사용하여 GET 요청
            ResponseEntity<Map> response = restTemplate.exchange(
                    new URI(apiUrl),
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            // 응답 상태 코드 확인
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();
                log.info("API 호출 성공: {}", responseBody);
                return responseBody != null ? responseBody.toString() : "응답이 비어있습니다.";
            } else {
                log.error("API 호출 실패: 상태 코드 {}", response.getStatusCode());
                return "API 호출 실패: 상태 코드 " + response.getStatusCode();
            }
        } catch (Exception e) {
            log.error("API 호출 실패: {}", e.getMessage());
            log.info("apiUrl: {}", apiUrl);
            return "API 호출 실패: " + e.getMessage();
        }
    }
}
