package com.sparta3tm.gatewayserver.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:19092");
        config.addAllowedOrigin("http://localhost:19093");
        config.addAllowedOrigin("http://localhost:19094");
        config.addAllowedOrigin("http://localhost:19095");
        config.addAllowedMethod("*"); // 허용할 HTTP 메서드, 예시로 모든 메서드 허용
        config.addAllowedHeader("*"); // 허용할 Header, 예시로 모든 Header 허용
        config.setAllowCredentials(true); // 쿠키와 같은 자격 증명 허용 여부

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 CORS 설정 적용

        return new CorsWebFilter(source);
    }
}
