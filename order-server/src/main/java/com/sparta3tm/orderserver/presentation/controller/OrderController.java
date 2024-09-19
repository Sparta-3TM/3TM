package com.sparta3tm.orderserver.presentation.controller;

import com.sparta3tm.common.support.response.ApiResponse;
import com.sparta3tm.orderserver.application.dto.request.order.OrderListRequestDto;
import com.sparta3tm.orderserver.application.dto.request.order.UpdateAmountOrderDto;
import com.sparta3tm.orderserver.application.dto.response.order.OrderResponseDto;
import com.sparta3tm.orderserver.application.service.OrderDeliveryService;
import com.sparta3tm.orderserver.application.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order API")
@Slf4j(topic = "order-controller")
public class OrderController {

    private final OrderDeliveryService orderDeliveryService;
    private final OrderService orderService;
    private final String USER_ID = "X-USER-ID";
    private final String USER_ROLE = "X-USER-ROLE";

    @Operation(summary = "Order Create")
    @PostMapping
    public ApiResponse<?> createOrder(@RequestBody OrderListRequestDto orderRequestDto,
                                      @RequestHeader(USER_ID) String userId,
                                      @RequestHeader(USER_ROLE) String userRole) {
        orderDeliveryService.createOrder(orderRequestDto, userId, userRole);
        return ApiResponse.success();
    }

    @Operation(summary = "Order Get One By Id")
    @GetMapping("/{orderId}")
    public ApiResponse<?> searchOrderById(@PathVariable Long orderId,
                                          @RequestHeader(USER_ID) String userId,
                                          @RequestHeader(USER_ROLE) String userRole) {
        OrderResponseDto data = orderService.searchOrderById(orderId, userId, userRole);
        return ApiResponse.success(data);
    }

    @Operation(summary = "Order Search")
    @GetMapping
    public ApiResponse<?> searchOrderList(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "createdAt") String sort,
                                          @RequestParam(defaultValue = "DESC") String direction,
                                          @RequestHeader(name = USER_ID) String userId,
                                          @RequestHeader(USER_ROLE) String userRole) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        List<OrderResponseDto> data = orderService.searchOrderList(pageable, userId, userRole);
        return ApiResponse.success(data);
    }

    @Operation(summary = "Order Update")
    @PutMapping("/{deliveryId}/{orderId}")
    public ApiResponse<?> updateOrder(@PathVariable Long orderId,
                                      @PathVariable Long deliveryId,
                                      @RequestBody UpdateAmountOrderDto updateAmountOrderDto,
                                      @RequestHeader(name = USER_ID) String userId,
                                      @RequestHeader(USER_ROLE) String userRole) {
        OrderResponseDto data = orderDeliveryService.updateOrder(orderId, deliveryId, updateAmountOrderDto, userId, userRole);
        return ApiResponse.success(data);
    }

    @Operation(summary = "Order Delete")
    @DeleteMapping("/{deliveryId}/{orderId}")
    public ApiResponse<?> deleteOrder(@PathVariable Long orderId,
                                      @PathVariable Long deliveryId,
                                      @RequestHeader(name = USER_ID) String userId,
                                      @RequestHeader(USER_ROLE) String userRole) {
        orderDeliveryService.deleteOrder(orderId, deliveryId, userId, userRole);
        return ApiResponse.success();
    }

}
