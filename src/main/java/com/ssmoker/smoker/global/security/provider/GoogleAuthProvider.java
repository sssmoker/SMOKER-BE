package com.ssmoker.smoker.global.security.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssmoker.smoker.global.security.exception.AuthException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.global.security.authDTO.GoogleProfile;
import com.ssmoker.smoker.global.security.authDTO.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GoogleAuthProvider {
    @Value("${GOOGLE_CLIENT_ID}")
    private String client;

    @Value("${GOOGLE_REDIRECT_URI}")
    private String redirect;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String clientSecret;

    // code로 access 토큰 요청하기
    public OAuthToken requestToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        String decode = URLDecoder.decode(code, StandardCharsets.UTF_8);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", decode);
        params.add("client_id", client);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirect);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> GoogleTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://oauth2.googleapis.com/token", // 구글 토큰 요청 URL
                        HttpMethod.POST,
                        GoogleTokenRequest,
                        String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        OAuthToken oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_GOOGLE);
        }

        return oAuthToken;
    }

    // Token으로 정보 요청하기
    public GoogleProfile requestGoogleProfile(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<String> googleProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://www.googleapis.com/oauth2/v3/userinfo", // Google 사용자 정보 요청 URL
                        HttpMethod.GET,
                        googleProfileRequest,
                        String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleProfile googleProfile = null;

        try {
            googleProfile = objectMapper.readValue(response.getBody(), GoogleProfile.class);
        } catch (JsonProcessingException e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_GOOGLE);
        }

        System.out.println(googleProfile.getEmail()); // 이메일 출력 예시
        return googleProfile;
    }
}
