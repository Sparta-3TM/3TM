package com.sparta3tm.hubserver.infrastructure.naver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta3tm.hubserver.infrastructure.naver.dto.RouteResponseDto;
import com.sparta3tm.hubserver.infrastructure.naver.dto.RouteResponseDto.Route.Trafast.Summary;
import com.sparta3tm.hubserver.infrastructure.naver.dto.StopoverDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j(topic = "naver-service")
@Service
@RequiredArgsConstructor
public class NaverService {

    private final RestTemplate restTemplate;

    @Value("${naver.map.client_id}")
    private String clientId;
    @Value("${naver.map.client_secret}")
    private String clientSecret;

    public List<StopoverDto> naverApi(String[] str, int stopoverNum) throws JsonProcessingException {
        String tmp = "";
        if (stopoverNum != 0) tmp = str[2];

        log.debug("naverAPI method Input: {} , {}", Arrays.toString(str), stopoverNum);

        URI uri = UriComponentsBuilder
                .fromUriString("https://naveropenapi.apigw.ntruss.com")
                .path("/map-direction/v1/driving")
                .queryParam("start", str[0])
                .queryParam("goal", str[1])
                .queryParam("waypoints", tmp)
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri)
                .header("X-NCP-APIGW-API-KEY-ID", clientId)
                .header("X-NCP-APIGW-API-KEY", clientSecret)
                .build();

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        RouteResponseDto data = objectMapper.readValue(responseEntity.getBody(), RouteResponseDto.class);


        Summary summary = data.getRoute().getTraoptimal().get(0).getSummary();


        List<StopoverDto> result = new ArrayList<>();

        Double distance = 0D;
        Double duration = 0D;

        for (int i = 0; i <= stopoverNum; i++) {
            log.debug(i + 1 + " 번째 경유지까지의 거리 = {} km", summary.getWaypoints().get(i).getDistance() / 1000D);
            log.debug(i + 1 + " 번째 경유지까지의 소요시간 = {} h", summary.getWaypoints().get(i).getDuration() / 1000D / 3600);

            distance += summary.getWaypoints().get(i).getDistance();
            duration += summary.getWaypoints().get(i).getDuration();
            result.add(new StopoverDto(summary.getWaypoints().get(i).getDistance() / 1000D, summary.getWaypoints().get(i).getDuration() / 1000D / 3600));
        }

        log.debug(stopoverNum + 1 + " 번째 경유지까지의 거리 = {} km", ((summary.getDistance() - distance) / 1000D));
        log.debug(stopoverNum + 1 + " 번째 경유지까지의 소요시간 = {} h", ((summary.getDuration() - duration) / 1000D / 3600));


        log.debug("최종 목적지까지의 거리 = {} km", summary.getDistance() / 1000D);
        log.debug("최종 목적지까지의 소요시간 = {} h", summary.getDuration() / 1000D / 3600);

        result.add(new StopoverDto((summary.getDistance() - distance) / 1000D, (summary.getDuration() - duration) / 1000D / 3600));
        result.add(new StopoverDto(summary.getDistance() / 1000D, summary.getDuration() / 1000D / 3600));
        return result;
    }

}
