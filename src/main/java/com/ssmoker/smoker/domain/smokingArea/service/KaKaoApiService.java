package com.ssmoker.smoker.domain.smokingArea.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.ssmoker.smoker.domain.smokingArea.dto.KaKaoApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
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
    @Value("${KAKAO_CLIENT_ID}")
    private String kakaoApiKey;

    private static final String KAKAO_API_URL
            = "https://dapi.kakao.com/v2/local/search/keyword.json?query=";

    public KaKaoApiResponse.KaKaoResponse getCenterLocationFromKakao (String keyword) {
        RestTemplate restTemplate = new RestTemplate();
        //String apiUrl = KAKAO_API_URL + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String apiUrl = KAKAO_API_URL + keyword;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.set("Accept", "application/json");
        HttpEntity<?> entity = new HttpEntity<>(headers);

            /*ResponseEntity<String> response =
                    restTemplate.exchange(apiUrl, HttpMethod.GET,
                            entity, String.class);*/

        ResponseEntity<KaKaoApiResponse> response
                = restTemplate.exchange(
                        apiUrl,
                HttpMethod.GET,
                entity,
                KaKaoApiResponse.class);

        System.out.println("response.getBody().getDocuments()" + response.getBody().getDocuments());
        System.out.println("response.getStatusCode()" + response.getStatusCode());

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
}
