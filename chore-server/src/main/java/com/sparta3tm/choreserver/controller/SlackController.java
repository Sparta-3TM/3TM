package com.sparta3tm.choreserver.controller;

import com.sparta3tm.choreserver.application.SlackService;
import com.sparta3tm.choreserver.application.dtos.slack.SlackMessageReqDto;
import com.sparta3tm.common.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
@Tag(name = "Slacks", description = "Slack API")
@Slf4j(topic = "SlackController")
public class SlackController {

    private final SlackService slackService;

    @Operation(summary = "Slack Direct Message 전송 API")
    @PostMapping("/message")
    public ApiResponse<?> sendSlackMessage(@RequestBody SlackMessageReqDto slackMessageReqDto){
        return ApiResponse.success(slackService.sendSlackMessage(slackMessageReqDto));
    }

    @Operation(summary = "날씨 정보 API")
    @GetMapping("/weather/{date}")
    public ApiResponse<?> getWeatherInfo(@PathVariable(name = "date") String date){
        return ApiResponse.success(slackService.getWeatherInfo(date));
    }

    @Operation(summary = "오늘의 날씨 정보 요약 전송 API")
    @GetMapping("/weather/today")
    public ApiResponse<?> getWeatherToday(){
        return ApiResponse.success(slackService.sendWeatherInfo());
    }

}
