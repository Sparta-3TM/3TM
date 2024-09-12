package com.sparta3tm.gatewayserver.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtFilter implements GlobalFilter {

    private final RedisTemplate<String, String> redisTemplate;
    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();

        // 회원가입 및 로그인 경로는 필터를 거치지 않도록 예외 처리
        if (requestPath.equals("/api/users/signUp") || requestPath.equals("/api/users/login")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return this.onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);  // "Bearer " 이후의 JWT 토큰 추출

        // Redis에서 캐싱된 토큰 확인
        String cachedToken = redisTemplate.opsForValue().get(token);
        if (cachedToken != null) {
            // 캐싱된 토큰이 있으면 바로 요청을 통과시킴
            return chain.filter(exchange);
        }

        // 캐싱된 토큰이 없을 경우 auth-server로 검증 요청
        return webClientBuilder.build()
                .post()
                .uri("http://auth-server/auth/validate-token")  // auth-server로 토큰 검증 요청
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(Boolean.class)  // 검증 결과는 Boolean
                .flatMap(isValid -> {
                    if (Boolean.TRUE.equals(isValid)) {
                        // 검증 성공 시 Redis에 토큰 캐싱
                        redisTemplate.opsForValue().set(token, token);
                        return chain.filter(exchange);
                    } else {
                        return this.onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
                    }
                });
    }

    // 에러 처리 메소드
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(err.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}

