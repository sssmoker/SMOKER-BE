package com.ssmoker.smoker.domain.smokingArea.controller;

import com.ssmoker.smoker.domain.smokingArea.domain.Location;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/open-api")
public class OpenApiController {

    private final static String API_KEY = "디코딩키";
    private final static String BASE_URL = "https://api.odcloud.kr/api/15090343/v1/uddi:7f5d9c71-fdc4-4a83-8c60-fa980eb70465";
    private final static String BASE_URL2 = "https://api.odcloud.kr/api/15040615/v1/uddi:d494c578-f45e-4c42-9dde-c277cbd8717a";
    private final static int PER_PAGE = 100;

    private final RestTemplate restTemplate;
    private final SmokingAreaRepository smokingAreaRepository;
    // 디코딩된 API 키 사용 (포털에서 제공된 Decoding Key)
    /*
    todo 지오코딩추가 (클래스 분리해서 메서드 제공하는 식으로하는게 좋을듯) , 예외 처리 , case 분리 방식
     */
    @GetMapping("/test")
    public String test() throws MalformedURLException {
        // 이미 인코딩된 키
        String preEncodedKey = "";
        String perEncodeKey2 = "";

        boolean hasNextPage = true;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, "application/json");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            int pageNumber = 1;
            while (hasNextPage) {
                String apiUrl = BASE_URL2 +
                        "?page=" + pageNumber +
                        "&perPage=" + PER_PAGE +
                        "&serviceKey=" + perEncodeKey2;

                ResponseEntity<Map> response = restTemplate.exchange(
                        new URI(apiUrl),
                        HttpMethod.GET,
                        entity,
                        Map.class
                );
                if (response.getStatusCode().is2xxSuccessful()) {
                    Map<String, Object> responseBody = response.getBody();
                    List<Map<String, Object>> dataList = (List<Map<String, Object>>) responseBody.get("data");

                    if (dataList.isEmpty()) {
                        hasNextPage = false;  // 데이터가 없으면 종료
                    } else {
                        storeOpenData(dataList);
                        pageNumber++;  // 다음 페이지로 이동
                    }
                }
            }
            return "저장완료";
        } catch (Exception e) {
            log.error("API 호출 실패: {}", e.getMessage());
            return "API 호출 실패: " + e.getMessage();
        }
    }

    private void storeOpenData(List<Map<String, Object>> dataList) {
        boolean hasLocation = false;
        boolean hasAddress = false;
        // 한 페이지 내부 데이터 저장 로직
        // fixme 서비스로 분리 또는 메서드로 분리
        for (Map<String, Object> data : dataList) {
            Pattern pattern = Pattern.compile(".*도로.*");
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
            // case 1 : 위도 경도만 있는 경우 (서울시만 하면 이 경우는 없다.)
            // case 2 : 도로명만 있는 경우
            // case 3 : 둘 다 있는 경우 - 지오코딩 x
            // 파라미터 검증 로직
            if (address == null || latitude == null || longitude == null) {
                continue;
            }
            Location location = new Location(address, latitude, longitude);
            SmokingArea smokingArea = new SmokingArea(addressName, location);
            smokingAreaRepository.save(smokingArea);
        }
    }
}
