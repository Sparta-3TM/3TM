package com.sparta3tm.hubserver.infrastructure.naver.dto;

import lombok.Data;

import java.util.List;

@Data
public class RouteResponseDto {
    private int code; // 응답 결과 코드
    private String message; // 응답 결과 문자열
    private String currentDateTime; // 탐색 시점 시간 정보
    private Route route; // Route 객체

    @Data
    public static class Route {
        private List<Trafast> traoptimal; // 경로 정보 리스트

        @Data
        public static class Trafast {
            private Summary summary; // 요약 정보
            private List<List<Double>> path; // 경로를 구성하는 모든 좌표열
            private List<Section> section; // 주요 도로 정보열
            private List<Guide> guide; // 안내 정보열

            @Data
            public static class Summary {
                private Position start; // 출발지
                private Position goal; // 목적지
                private List<Position> waypoints; // 경유지 리스트
                private int distance; // 전체 경로 거리 (meters)
                private long duration; // 전체 경로 소요 시간 (milliseconds)
                private List<List<Double>> bbox; // 전체 경로 경계 영역
                private int tollFare; // 통행 요금
                private int taxiFare; // 택시 요금
                private int fuelPrice; // 유류비
                private String departureTime; // 추가된 departureTime 필드

                @Data
                public static class Position {
                    private List<Double> location; // 지점 (lng-lat)
                    private Integer dir; // 경로상에서 바라보는 방향 (optional)
                    private Integer distance; // 출발지 또는 직전 경유지부터의 거리 (optional)
                    private Integer duration; // 소요 시간 (optional)
                    private Integer pointIndex; // 인덱스 (optional)
                }
            }

            @Data
            public static class Section {
                private int pointIndex; // 경로를 구성하는 좌표의 인덱스
                private int pointCount; // 형상점 수
                private int distance; // 거리 (meters)
                private String name; // 도로명
                private Integer congestion; // 혼잡도 (optional)
                private Integer speed; // 평균 속도 (optional)
            }

            @Data
            public static class Guide {
                private int pointIndex; // 경로를 구성하는 좌표의 인덱스
                private int type; // 안내 종류
                private String instructions; // 안내 문구 (optional)
                private int distance; // 이전 guide unit까지의 거리 (meters)
                private long duration; // 이전 guide unit까지의 소요 시간 (milliseconds)
            }
        }
    }
}