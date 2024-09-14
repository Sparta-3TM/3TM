package com.sparta3tm.hubserver.infrastructure.naver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverMapService {

    private final RestTemplate restTemplate;

    @Value("${naver.map.client_id}")
    private String clientId;
    @Value("${naver.map.client_secret}")
    private String clientSecret;

    public void naverApi(LinkedHashMap<Double, Double> map) {



//        for (Map.Entry<Double, Double> entry : map.entrySet()) {
//
//        }

        URI uri = UriComponentsBuilder
                .fromUriString("https://naveropenapi.apigw.ntruss.com")
                .path("/map-direction/v1/driving")
                .queryParam("start", "")
                .queryParam("goal", "")
                .queryParam("waypoints", "")
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri)
                .header("X-NCP-APIGW-API-KEY-ID", clientId)
                .header("X-NCP-APIGW-API-KEY", clientSecret)
                .build();

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

//        restTemplate.
//        log.info("NAVER API Status Code : " + responseEntity.getStatusCode());


        /**
         * waypoints	multiple request position format list
         * - 경유지
         * 최대 5개를 입력할 수 있으며 서로 다른 경유지의 구분자로 |(pipe char)를 사용
         */


    }


}
