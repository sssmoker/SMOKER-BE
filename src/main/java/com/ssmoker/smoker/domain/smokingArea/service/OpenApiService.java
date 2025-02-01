package com.ssmoker.smoker.domain.smokingArea.service;

import static com.ssmoker.smoker.domain.smokingArea.domain.Feature.makeEmptyFeature;

import com.ssmoker.smoker.domain.smokingArea.domain.Feature;
import com.ssmoker.smoker.domain.smokingArea.domain.Location;
import com.ssmoker.smoker.domain.smokingArea.domain.SmokingArea;
import com.ssmoker.smoker.domain.smokingArea.dto.KaKaoApiResponse.KaKaoResponse;
import com.ssmoker.smoker.domain.smokingArea.repository.SmokingAreaRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenApiService {
    private static final String[] BASE_URLS = {
            "https://api.odcloud.kr/api/15040615/v1/uddi:d494c578-f45e-4c42-9dde-c277cbd8717a",   // 광진구
            "https://api.odcloud.kr/api/15040413/v1/uddi:a665ca8b-95d6-4ef8-a7c0-8e31c6b1b9a7",   // 서대문구
            "https://api.odcloud.kr/api/15040636/v1/uddi:4250aae2-a50a-4a5d-8d70-9ec4c55a92f3",   // 중랑구
            "https://api.odcloud.kr/api/15090343/v1/uddi:7f5d9c71-fdc4-4a83-8c60-fa980eb70465",   // 송파구
            "https://api.odcloud.kr/api/15069051/v1/uddi:2653cc01-60d7-4e8b-81f4-80b24a39d8f6",   // 영등포구
            "https://api.odcloud.kr/api/15080296/v1/uddi:87a3b7f3-fa03-4345-8001-b0fb950a1ab1",   // 중구
            "https://api.odcloud.kr/api/15073796/v1/uddi:17fbd06c-45bb-48aa-9be7-b26dbc708c9c"    // 용산구
    };

    private static final int PER_PAGE = 100;
    private static final String HAS_KEY = "키가 존재하지 않습니다";

    @Value("${open-api.key}")
    private String encodeKey;

    private final RestTemplate restTemplate;
    private final SmokingAreaRepository smokingAreaRepository;
    private final KaKaoApiService kaKaoApiService;

    public void getPublicData() throws URISyntaxException {
        for (String baseUrl : BASE_URLS) {
            int pageNumber = 1;
            boolean hasNextPage = true;

            while (hasNextPage) {
                ResponseEntity<Map> response = fetchDataFromApi(baseUrl, pageNumber, encodeKey);

                if (response.getStatusCode().is2xxSuccessful()) {
                    List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.getBody().get("data");

                    if (dataList.isEmpty()) {
                        hasNextPage = false; // 데이터가 없으면 종료
                    } else {
                        storeOpenData(dataList);
                        pageNumber++; // 다음 페이지로 이동
                    }
                }
            }
        }
    }

    private ResponseEntity<Map> fetchDataFromApi(String baseUrl, int pageNumber, String apiKey)
            throws URISyntaxException {
        String apiUrl = baseUrl + "?page=" + pageNumber + "&perPage=" + PER_PAGE + "&serviceKey=" + apiKey;

        return restTemplate.exchange(
                new URI(apiUrl),
                HttpMethod.GET,
                getHttpEntity(),
                Map.class
        );
    }

    private HttpEntity<?> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, "application/json");
        return new HttpEntity<>(headers);
    }

    private void storeOpenData(List<Map<String, Object>> dataList) {
        for (Map<String, Object> data : dataList) {
            String addressKey = findKey(data, List.of("도로", "주소")).orElse(HAS_KEY);
            String areaTypeKey = findKey(data, List.of("형태", "구분")).orElse(HAS_KEY);

            String address = (String) data.getOrDefault(addressKey, null);
            String areaType = (String) data.getOrDefault(areaTypeKey, null);
            Double latitude = convertToDouble(data.get("위도"));
            Double longitude = convertToDouble(data.get("경도"));

            if ((latitude == null || longitude == null) && address != null) {
                try {
                    String processedAddress = address.split("[,(]")[0].trim();

                    KaKaoResponse location = kaKaoApiService.getCenterLocationFromKakao(processedAddress);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                } catch (RuntimeException e) {
                    log.warn("지오코딩 실패: {}", address);
                    continue;
                }
            }
            if ((latitude != null && longitude != null) && address == null) {
                address = kaKaoApiService.getAddress(latitude, longitude);
                if (address == null) {
                    log.warn("지오코딩 실패: {},{}", latitude, longitude);
                    continue;
                }
                log.info("address: {}", address);
            }

            if (address == null || latitude == null || longitude == null) {
                continue; // 잘못된 데이터는 무시
            }

            Location location = new Location(address, latitude, longitude);
            Feature feature = makeEmptyFeature();
            SmokingArea smokingArea = new SmokingArea(address, location, feature, areaType);
            smokingAreaRepository.save(smokingArea);
        }
    }

    private Optional<String> findKey(Map<String, Object> data, List<String> priorityKeys) {
        return priorityKeys.stream()
                .flatMap(priority -> data.keySet().stream().filter(key -> key.contains(priority)))
                .findFirst();
    }

    private Double convertToDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Double) {
            return (Double) value;  // 이미 Double이면 그대로 반환
        }
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value); // String이면 변환 시도
            } catch (NumberFormatException e) {
                log.warn("Double 변환 실패: {}", value);
                return null;  // 변환 실패 시 null 반환
            }
        }
        return null;  // 다른 타입이면 null 반환
    }
}