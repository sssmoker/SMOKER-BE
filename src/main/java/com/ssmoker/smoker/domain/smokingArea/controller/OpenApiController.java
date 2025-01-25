package com.ssmoker.smoker.domain.smokingArea.controller;

import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.URI;

import com.ssmoker.smoker.domain.smokingArea.domain.Feature;
import com.ssmoker.smoker.domain.smokingArea.domain.Location;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    private final SmokingAreaRepository smokingAreaRepository;
    // 디코딩된 API 키 사용 (포털에서 제공된 Decoding Key)
    private final static String API_KEY = "디코딩키";
    private final static String BASE_URL = "https://api.odcloud.kr/api/15090343/v1/uddi:7f5d9c71-fdc4-4a83-8c60-fa980eb70465";

    @GetMapping("/test")
    public String test() throws MalformedURLException {
        // 이미 인코딩된 키
        String preEncodedKey = "";

        String apiUrl = BASE_URL +
                "?page=1" +
                "&perPage=10&serviceKey=" +
                preEncodedKey;

        try {
            // 헤더 설정 (필요 시 추가)
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, "application/json");

            // HttpEntity 생성
            HttpEntity<?> entity = new HttpEntity<>(headers);

            // RestTemplate 을 사용하여 GET 요청
            ResponseEntity<Map> response = restTemplate.exchange(
                    new URI(apiUrl),
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            // 응답 상태 코드 확인
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) responseBody.get("data");
                boolean hasLocation = false;
                boolean hasAddress = false;
                for (Map<String, Object> data : dataList) {
                    Pattern pattern = Pattern.compile(".*도로명.*");
                    String addressName = null;
                    String address = null;
                    Double latitude = 0.0;
                    Double longitude = 0.0;
                    // 키 존재 여부 및 해당 키 이름 가져오기
                    String foundKey = data.keySet().stream()
                            .filter(key -> pattern.matcher(key).matches())  // 정규식과 일치하는 키 찾기
                            .findFirst()  // 첫 번째 일치 항목 가져오기
                            .orElse("키가 존재하지 않습니다");

                    if (data.containsKey("위도") && data.containsKey("경도")) {
                        hasLocation = true;
                        latitude = (Double) data.get("위도");
                        longitude = (Double) data.get("경도");
                    }
                    if (!foundKey.equals("키가 존재하지 않습니다")) {
                        hasAddress = true;
                        address = (String) data.get(foundKey);
                        addressName = address;
                    }
                    // case 1 : 위도 경도만 있는 경우
                    // case 2 : 도로명만 있는 경우
                    // case 3 : 둘 다 있는 경우 - 지오코딩 x
                    // 파라미터 검증 로직
                    Location location = new Location(address,latitude,longitude);
                    Feature feature = new Feature(null,null,null,null);
                    SmokingArea smokingArea = new SmokingArea(addressName, location);
                    smokingAreaRepository.save(smokingArea);
                }
                return responseBody.toString();
            } else {
                return "API 호출 실패: 상태 코드 " + response.getStatusCode();
            }
        } catch (Exception e) {
            log.error("API 호출 실패: {}", e.getMessage());
            return "API 호출 실패: " + e.getMessage();
        }
    }
}
