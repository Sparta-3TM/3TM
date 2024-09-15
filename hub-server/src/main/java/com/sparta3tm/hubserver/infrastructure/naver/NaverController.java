//package com.sparta3tm.hubserver.infrastructure.naver;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.sparta3tm.common.support.response.ApiResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class NaverController {
//
//    private final NaverService naverMapService;
//    @GetMapping("/api/naver")
//    public ApiResponse<?> naverApi() throws JsonProcessingException {
//        RouteResponseDto data = naverMapService.naverApi(new String[]{
//                "127.1193079,37.4857357",
//                "126.8740591,37.6406219",
//                "127.3759637,37.1903762|129.0430469,35.1157187"
//        }, 2);
//        return ApiResponse.success(data);
//    }
//
//}
