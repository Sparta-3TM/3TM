package com.sparta3tm.choreserver.application;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SlackService")
@Transactional(readOnly = true)
public class SlackService {

    private final SlackRepository slackRepository;
    private final RestTemplate slackRestTemplate;
    private final WeatherService weatherService;
    private final GeminiService geminiService;

    @Value("${slack.incoming-hook.url}")
    String slackURL;

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

    @Transactional
    @Scheduled(cron = "0 0 6 * * ?") // 매일 오전 6시 스케줄링
    public String sendWeatherInfo(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String today = LocalDate.now().format(formatter);

        String requestText = "";
        try{
            requestText = weatherService.getWeatherInfo(today);
        }catch (Exception e){
            log.error(e.getMessage());
            log.error("Internal Server Error");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        // 날씨 정보 요약 요청
        String result = geminiService.getGeminiSummary(requestText + " 의 정보를 요약해서 오늘의 날씨를 알려주고 " +
                "Slack mrkdwn 서식을 적용해서 포맷을 예쁘게 꾸미고 " +
                "Slack mrkdwn 서식 적용 과정에서 Bold 처리는 반드시 *와 여백 한개로 감싸야 해 " +
                "(예시)오늘은 *흐리고 비* 가 내릴 예정)" +
                "그리고 이모티콘도 넣고 줄바꿈도 잘 적용해서 " +
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
