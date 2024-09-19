package com.sparta3tm.hubserver.infrastructure.data;

import com.sparta3tm.hubserver.domain.entity.Hub;
import com.sparta3tm.hubserver.domain.entity.HubMovementInfo;
import com.sparta3tm.hubserver.domain.repository.HubRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final HubRepository hubRepository;

    @PostConstruct
    public void init() {
        List<Hub> hubList = new ArrayList<>();
        hubList.add(new Hub("서울특별시 센터", "서울특별시 송파구 송파대로 55", 37.4857357, 127.1193079));
        hubList.add(new Hub("경기 북부 센터", "경기도 고양시 덕양구 권율대로 570", 37.6406219, 126.8740591));
        hubList.add(new Hub("경기 남부 센터", "경기도 이천시 덕평로 257-21", 37.1903762, 127.3759637));
        hubList.add(new Hub("부산광역시 센터", "부산 동구 중앙대로 206", 35.1157187, 129.0430469));
        hubList.add(new Hub("대구광역시 센터", "대구 북구 태평로 161", 35.8757852, 128.5957107));
        hubList.add(new Hub("인천광역시 센터", "인천 남동구 정각로 29", 37.4558217, 126.7063549));
        hubList.add(new Hub("광주광역시 센터", "광주 서구 내방로 111", 35.159902, 126.8517686));
        hubList.add(new Hub("대전광역시 센터", "대전 서구 둔산로 100", 36.3502088, 127.3847266));
        hubList.add(new Hub("울산광역시 센터", "울산 남구 중앙로 201", 35.538872, 129.3115985));
        hubList.add(new Hub("세종특별자치시 센터", "세종특별자치시 한누리대로 2130", 36.479865, 127.2888981));
        hubList.add(new Hub("강원특별자치도 센터", "강원특별자치도 춘천시 중앙로 1", 37.8824849, 127.7290281));
        hubList.add(new Hub("충청북도 센터", "충북 청주시 상당구 상당로 82", 36.6353921, 127.4915033));
        hubList.add(new Hub("충청남도 센터", "충남 홍성군 홍북읍 충남대로 21", 36.6587376, 126.6737074));
        hubList.add(new Hub("전북특별자치도 센터", "전북특별자치도 전주시 완산구 효자로 225", 35.8200676, 127.1091435));
        hubList.add(new Hub("전라남도 센터", "전남 무안군 삼향읍 오룡길 1", 34.8152577, 126.4573995));
        hubList.add(new Hub("경상북도 센터", "경북 안동시 풍천면 도청대로 455", 36.5753684, 128.5054304));
        hubList.add(new Hub("경상남도 센터", "경남 창원시 의창구 중앙대로 300", 35.2370703, 128.6912893));
        hubRepository.saveAll(hubList);

        List<HubMovementInfo> hubMovementInfoList = new ArrayList<>();
        hubMovementInfoList.add(new HubMovementInfo(1L, 2L, LocalTime.of(0, 23, 0), 21D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 3L, LocalTime.of(0, 50, 0), 27D));

        hubMovementInfoList.add(new HubMovementInfo(1L, 4L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 5L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 6L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 7L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 8L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 9L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 10L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 11L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 12L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 13L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 14L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 15L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 16L, LocalTime.of(0, 50, 0), 27D));
        hubMovementInfoList.add(new HubMovementInfo(1L, 17L, LocalTime.of(0, 50, 0), 27D));



    }

}
