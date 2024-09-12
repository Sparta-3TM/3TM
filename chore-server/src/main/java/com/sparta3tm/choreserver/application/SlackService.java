package com.sparta3tm.choreserver.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta3tm.choreserver.application.dtos.slack.SlackIncomingHookDto;
import com.sparta3tm.choreserver.application.dtos.slack.SlackMessageReqDto;
import com.sparta3tm.choreserver.application.dtos.slack.SlackMessageResDto;
import com.sparta3tm.choreserver.application.dtos.weather.WeatherInfoResDto;
import com.sparta3tm.choreserver.domain.slack.Slack;
import com.sparta3tm.choreserver.domain.slack.SlackRepository;
import com.sparta3tm.common.gemini.GeminiReqDto;
import com.sparta3tm.common.gemini.GeminiResDto;
import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SlackService")
@Transactional(readOnly = true)
public class SlackService {

    private final SlackRepository slackRepository;
    private final RestTemplate slackRestTemplate;
    private final RestTemplate geminiRestTemplate;
    private final RestTemplate weatherRestTemplate;

    @Value("${weather.service.key}")
    String weatherServiceKey;

    @Value("${slack.incoming-hook.url}")
    String slackURL;

    @Value("${gemini.api.key}")
    String geminiApiKey;

    @Transactional
    public SlackMessageResDto sendSlackMessage(SlackMessageReqDto slackMessageReqDto) {
        try{
            SlackIncomingHookDto request = new SlackIncomingHookDto("@"+slackMessageReqDto.getReceiverId(), slackMessageReqDto.getMessage());
            log.info(slackRestTemplate.postForObject(slackURL, request, String.class));
            return SlackMessageResDto.from(slackRepository.save(slackMessageReqDto.toEntity(LocalDateTime.now())));
        }catch (Exception e){
            log.error("Internal Server Error");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }
    }

    public WeatherInfoResDto getWeatherInfo(String date){
        String requestURL = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?" +
                "serviceKey=" + weatherServiceKey +
                "&pageNo=1" +
                "&numOfRows=217" +
                "&dataType=JSON" +
                "&base_date="+ date +
                "&base_time=0500" +
                "&nx=63" +
                "&ny=124";
        log.info(requestURL);
        WeatherInfoResDto info = weatherRestTemplate.getForObject(requestURL, WeatherInfoResDto.class);
        log.info(info.toString());
        return info;
    }

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

    @Transactional
    @Scheduled(cron = "0 0 6 * * ?") // 매일 오전 6시 스케줄링
    public String sendWeatherInfo(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String today = LocalDate.now().format(formatter);

        String requestText = "";
        try{
            requestText = getWeatherInfo(today).getInfos();
            log.info("날씨 정보 호출 성공!");
        }catch (Exception e){
            log.error("Internal Server Error");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        // 날씨 정보 요약 요청
        String result = getGeminiSummary(requestText + " 의 정보를 요약해서 오늘의 날씨를 알려주고 " +
                "포맷을 예쁘게 꾸미고 이모티콘도 넣고 줄바꿈도 잘 적용해서 " +
                "슬렉 메시지 전송용으로 " +
                "한글로 500자 이내로 작성해줘", today);

        try{
            SlackIncomingHookDto slackRequest = new SlackIncomingHookDto("오늘의-날씨", result);
            log.info(slackRestTemplate.postForObject(slackURL, slackRequest, String.class));
            slackRepository.save(Slack.builder().receiverId("오늘의-날씨").message(result).sendTime(LocalDateTime.now()).build());
        }catch (Exception e){
            log.error("Internal Server Error");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        return result;
    }
}
