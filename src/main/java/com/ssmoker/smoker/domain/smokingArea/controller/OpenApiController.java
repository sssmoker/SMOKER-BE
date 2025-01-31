package com.ssmoker.smoker.domain.smokingArea.controller;

import static com.ssmoker.smoker.domain.smokingArea.domain.Feature.makeEmptyFeature;

import com.ssmoker.smoker.domain.smokingArea.domain.Feature;
import com.ssmoker.smoker.domain.smokingArea.domain.Location;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.dto.KaKaoApiResponse.KaKaoResponse;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import com.ssmoker.smoker.domain.smokingArea.service.KaKaoApiService;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private final static String BASE_URL = "https://api.odcloud.kr/api/15040615/v1/uddi:d494c578-f45e-4c42-9dde-c277cbd8717a";
    private final static int PER_PAGE = 100;

    @Value("${open-api.key}")
    private String encodeKey;

    private final RestTemplate restTemplate;
    private final SmokingAreaRepository smokingAreaRepository;
    private final KaKaoApiService kaKaoApiService;


    @GetMapping("/test")
    public String test() throws MalformedURLException {

        boolean hasNextPage = true;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, "application/json");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            int pageNumber = 1;
            while (hasNextPage) {
                String apiUrl = BASE_URL +
                        "?page=" + pageNumber +
                        "&perPage=" + PER_PAGE +
                        "&serviceKey=" + encodeKey;

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
            e.printStackTrace();
            return "API 호출 실패: " + e.getMessage();
        }
    }

    private void storeOpenData(List<Map<String, Object>> dataList) {
        // 한 페이지 내부 데이터 저장 로직
        // fixme 서비스로 분리 또는 메서드로 분리
        for (Map<String, Object> data : dataList) {
            Pattern pattern = Pattern.compile(".*도로.*");
            String addressName = null;
            String address = null;
            Double latitude = null;
            Double longitude = null;
            // 키 존재 여부 및 해당 키 이름 가져오기
            String foundKey = data.keySet().stream()
                    .filter(key -> pattern.matcher(key).matches())  // 정규식과 일치하는 키 찾기
                    .findFirst()  // 첫 번째 일치 항목 가져오기
                    .orElse("키가 존재하지 않습니다");

            if (data.containsKey("위도") && data.containsKey("경도")) {
                latitude = (Double) data.get("위도");
                longitude = (Double) data.get("경도");
            }
            if (!foundKey.equals("키가 존재하지 않습니다")) {
                address = (String) data.get(foundKey);
                addressName = address;
            }

            // case : 도로명만 있는 경우 (지오코딩)
            if ((latitude == null || longitude == null) && address != null) {
                log.info("지오코딩 함 ");
                KaKaoResponse location = new KaKaoResponse();
                try {
                    location = kaKaoApiService.getCenterLocationFromKakao(address);
                } catch (RuntimeException e) {
                    continue;
                }
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            // 잘못된 case 넘긴다.
            if (address == null || latitude == null || longitude == null) {
                continue; //  pass
            }

            Location location = new Location(address, latitude, longitude);
            Feature feature = makeEmptyFeature();
            SmokingArea smokingArea = new SmokingArea(addressName, location, feature);
            smokingAreaRepository.save(smokingArea);
        }
    }

}
