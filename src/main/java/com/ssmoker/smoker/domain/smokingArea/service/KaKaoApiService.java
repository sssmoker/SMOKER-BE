package com.ssmoker.smoker.domain.smokingArea.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssmoker.smoker.domain.smokingArea.dto.KaKaoApiResponse;
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
@RequiredArgsConstructor
@Service
public class KaKaoApiService {

    private static final String KAKAO_API_URL
            = "https://dapi.kakao.com/v2/local/search/keyword.json?query=";
    private static final String KAKAO_API_URL_FOR_ADDRESS
            = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?";

    @Value("${KAKAO_CLIENT_ID}")
    private String kakaoApiKey;

    public KaKaoApiResponse.KaKaoResponse getCenterLocationFromKakao(String keyword) {
        RestTemplate restTemplate = new RestTemplate();
        //String apiUrl = KAKAO_API_URL + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String apiUrl = KAKAO_API_URL + keyword;
        HttpEntity<?> entity = getHttpEntity();
            /*ResponseEntity<String> response =
                    restTemplate.exchange(apiUrl, HttpMethod.GET,
                            entity, String.class);*/

        ResponseEntity<KaKaoApiResponse> response
                = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                KaKaoApiResponse.class);

        //JSONObject jsonResponse = new JSONObject(response.getBody());

        //JsonArray documents = jsonResponse.getAsJsonArray("documents");

        if (response.getBody() != null && !response.getBody().getDocuments().isEmpty()) {
            KaKaoApiResponse.KaKaoResponse firstResult = response.getBody().getDocuments()
                    .get(0);

            return new KaKaoApiResponse.KaKaoResponse(
                    firstResult.getLatitude(), firstResult.getLongitude());
        }
        throw new RuntimeException("검색된 지역이 없습니다.");
    }

    public String getAddress(Double latitude, Double longitude) {
        final String apiUrl = KAKAO_API_URL_FOR_ADDRESS + "x=" + longitude + "&y=" + latitude;
        HttpEntity<?> entity = getHttpEntity();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                String.class // JSON을 String으로 받음
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());

                JsonNode documents = root.path("documents"); // "documents" 노드 가져오기
                if (documents.isArray() && documents.size() > 0) {
                    return documents.get(0).path("address_name").asText(); // 첫 번째 주소 반환
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return null; // 주소가 없을 경우
    }

    private HttpEntity<?> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.set("Accept", "application/json");
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return entity;
    }
}