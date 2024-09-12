package com.sparta3tm.choreserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate geminiRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public RestTemplate slackRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public RestTemplate weatherRestTemplate(){
        return new RestTemplate();
    }
}
