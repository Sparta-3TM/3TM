//package com.sparta3tm.hubserver.presentation.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.sparta3tm.common.support.response.ApiResponse;
//import com.sparta3tm.hubserver.application.dto.ai.PromptDto;
//import com.sparta3tm.hubserver.application.service.AiService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class AiController {
//
//    private final AiService aiService;
//
//    @PostMapping("/api/prompt")
//    public ApiResponse<?> prompt(@RequestBody PromptDto promptDto) throws JsonProcessingException {
//        String data = aiService.requestAi(promptDto.startId(), promptDto.endId());
//        return ApiResponse.success(data);
//    }
//}
