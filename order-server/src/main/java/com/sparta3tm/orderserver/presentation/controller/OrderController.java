package com.sparta3tm.orderserver.presentation.controller;

import com.sparta3tm.common.support.response.ApiResponse;
import com.sparta3tm.orderserver.application.dto.request.OrderRequestDto;
import com.sparta3tm.orderserver.application.service.OrderDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderDeliveryService orderService;

    @PostMapping
    public ApiResponse<?> createOrder(@RequestBody OrderRequestDto orderRequestDto,
                                      @RequestHeader("X-USER-ID") String userId) {
//        orderService.createOrder(orderRequestDto, userId);
        return ApiResponse.success();
    }
}
