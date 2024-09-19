# Sparta Java 단기 심화과정 물류 & 배송 MSA 프로젝트 - 3TM

---

## 프로젝트 구성원

- [송형근](https://github.com/lukeydokey)
- [전민기](https://github.com/awesominki)
- [최현](https://github.com/CZ-punk)

---

## 프로젝트 목적/상세

MSA 프로젝트를 활용하여 물류 배송 시스템 구축, **MSA의 복잡성을 이해**하고, **팀원들과 함께 MSA를 구축하며 실무에서 발생할 수 있는 문제를 간접적으로 경험하고 해결해 보기**

---

## 사전 준비

- Naver OpenAPI Client ID & Secret
- 기상청 예보 OpenAPI 사용신청 및 API Key 발급 ([기상청 예보 OpenAPI](https://www.data.go.kr/data/15084084/openapi.do))
- Gemini API Key 발급
- Slack App 생성
    - 오늘의-날씨 채널 생성
    - Incoming webhook 앱 추가
    - Incoming webhook URL 복사
- PostgreSQL 설치 (5432 포트)
- Redis 설치 (6379 포트)

---

## 서비스 구성 및 실행방법

![인프라 설계](https://file.notion.so/f/f/83c75a39-3aba-4ba4-a792-7aefe4b07895/8ba70b4c-45ad-4159-bc3b-d100cebcb427/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-09-19_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_2.35.38.png?table=block&id=da0e9d23-0e8f-4ac8-93be-68abcc5fb977&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&expirationTimestamp=1726819200000&signature=thJXRIkwwGE8vEUCI_Bim0aKjozRAuA6HJScnQDGYBE&downloadName=%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2024-09-19+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+2.35.38.png)

### Eureka Server

- 포트 : 19090
- 필요 환경변수 : X

### Gateway Server

- 포트 : 19091
- 필요 환경변수 : X

### Auth Server

- 포트 : 19092
- 필요 환경변수 : X

### Hub Server

- 포트 : 19093
- 필요 환경변수
    - `naver_map_client_id`
    - `naver_map_client_secret`

### Company Server

- 포트 : 19094
- 필요 환경변수
    - `GEMINI_API_KEY`

### Order Server

- 포트 : 19095
- 필요 환경변수 : X

### Chore Server

- 포트 : 19096
- 필요 환경변수
    - `WEATHER_SERVICE_KEY`
    - `SLACK_URL`
    - `GEMINI_API_KEY`

### PostgreSQL

- 포트 : 5432
- DB : postgres
- username : postgres
- pw : 1234

### Redis

- 포트 : 6379
- username : default
- pw : systempass

---

## 트러블 슈팅
- Gateway Multi Path 적용 X 이슈 - [Gateway Predicate Multi Paths](https://velog.io/@lukeydokey/Spring-Cloud-Gateway-Predicate-Multi-Paths)
- Multi Module 사용 간 의존성 - [Multi Module 공통 모듈 Dependencies](https://velog.io/@lukeydokey/Spring-Boot-Multi-Module-공통-모듈-Dependencies)

---


## ERD

![ERD](https://file.notion.so/f/f/83c75a39-3aba-4ba4-a792-7aefe4b07895/2ae98372-e628-49d2-b5c9-12e63649674f/image.png?table=block&id=a899b502-7deb-47d4-8a0b-f6385b561c9d&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&expirationTimestamp=1726819200000&signature=k8Ihx8dzeQSqPHgr-4ZDsh5-ynQRk82D4an3n3Lg8zA&downloadName=image.png)

---

## 기술 스택

- Spring Boot 3.3.3
- Spring Cloud
- JWT
- JPA
- Redis
- Spring Security
- PostgreSql
- Docker
- Swagger
- Slack

---

## 프로젝트 구조

```
.
├── auth-server
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── sparta3tm
│           │           └── authserver
│           │               ├── application
│           │               │   └── dtos
│           │               │       ├── DM
│           │               │       ├── auth
│           │               │       └── user
│           │               ├── config
│           │               ├── controller
│           │               └── domain
│           │                   ├── DM
│           │                   └── user
│           └── resources
├── chore-server
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── sparta3tm
│           │           └── choreserver
│           │               ├── application
│           │               │   └── dtos
│           │               │       ├── slack
│           │               │       └── weather
│           │               ├── config
│           │               ├── controller
│           │               └── domain
│           │                   └── slack
│           └── resources
├── common
│   └── src
│       └── main
│           └── java
│               └── com
│                   └── sparta3tm
│                       └── common
│                           ├── config
│                           ├── gemini
│                           └── support
│                               ├── error
│                               └── response
├── company-server
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── sparta3tm
│           │           └── companyserver
│           │               ├── application
│           │               │   └── dtos
│           │               │       ├── company
│           │               │       └── product
│           │               ├── config
│           │               ├── controller
│           │               ├── domain
│           │               │   ├── company
│           │               │   └── product
│           │               └── infrastructure
│           └── resources
├── eureka-server
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── sparta3tm
│           │           └── eurekaserver
│           └── resources
├── gateway-server
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── sparta3tm
│           │           └── gatewayserver
│           │               ├── config
│           │               ├── dto
│           │               └── filter
│           └── resources
├── hub-server
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── sparta3tm
│           │           └── hubserver
│           │               ├── application
│           │               │   ├── dto
│           │               │   │   ├── hmi
│           │               │   │   │   ├── request
│           │               │   │   │   └── response
│           │               │   │   └── hub
│           │               │   │       ├── request
│           │               │   │       └── response
│           │               │   └── service
│           │               ├── domain
│           │               │   ├── entity
│           │               │   └── repository
│           │               ├── infrastructure
│           │               │   ├── client
│           │               │   │   └── dto
│           │               │   ├── config
│           │               │   ├── data
│           │               │   └── naver
│           │               │       ├── dto
│           │               │       └── service
│           │               └── presentation
│           │                   ├── controller
│           │                   └── handler
│           └── resources
└── order-server
    └── src
        └── main
            ├── java
            │   └── com
            │       └── sparta3tm
            │           └── orderserver
            │               ├── application
            │               │   ├── dto
            │               │   │   ├── request
            │               │   │   │   ├── delivery
            │               │   │   │   └── order
            │               │   │   └── response
            │               │   │       ├── delivery
            │               │   │       └── order
            │               │   └── service
            │               ├── domain
            │               │   ├── entity
            │               │   │   ├── delivery
            │               │   │   ├── delivery_route
            │               │   │   └── order
            │               │   └── repository
            │               ├── infrastructure
            │               │   └── client
            │               │       └── dto
            │               │           ├── company
            │               │           └── hub
            │               └── presentation
            │                   └── controller
            └── resources
```