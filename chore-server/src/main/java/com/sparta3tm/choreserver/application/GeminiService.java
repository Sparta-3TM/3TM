package com.sparta3tm.choreserver.application;

import com.sparta3tm.common.gemini.GeminiReqDto;
import com.sparta3tm.common.gemini.GeminiResDto;
import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "GeminiService")
@Transactional(readOnly = true)
public class GeminiService {

    private final RestTemplate geminiRestTemplate;

    @Value("${gemini.api.key}")
    String geminiApiKey;

    @Cacheable(cacheNames = "WeatherSummary", key = "args[1]")
    public String getGeminiSummary(String requestPrompt, String date){
        String geminiURL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key="
                + geminiApiKey;

        GeminiReqDto request = new GeminiReqDto();

        request.createGeminiReqDto(requestPrompt);
        String result = "";

        try{
            GeminiResDto response = geminiRestTemplate.postForObject(geminiURL, request, GeminiResDto.class);
            log.info("Gemini 요청 성공");
            result = response.getCandidates().get(0).getContent().getParts().get(0).getText();
        }catch (Exception e){
            log.error(e.getMessage());
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        return result;
    }

}
