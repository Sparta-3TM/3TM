package com.sparta3tm.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfigurer implements WebMvcConfigurer {

    private final String[] ORIGIN_WHITE_LIST = {
            "http://localhost:19090/", "http://localhost:19091/", "http://localhost:19092/",
            "http://localhost:19093/", "http://localhost:19094/", "http://localhost:19095/"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(ORIGIN_WHITE_LIST)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*");
    }
}
