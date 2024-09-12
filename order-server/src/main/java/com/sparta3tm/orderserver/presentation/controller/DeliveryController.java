package com.sparta3tm.orderserver.presentation.controller;

import com.sparta3tm.common.support.response.ApiResponse;
import com.sparta3tm.orderserver.application.dto.request.delivery.DeliveryUpdateDto;
import com.sparta3tm.orderserver.application.dto.response.delivery.DeliveryResponseDto;
import com.sparta3tm.orderserver.application.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final String USER_ID = "X-USER-ID";
    private final String USER_ROLE = "X-USER-ROLE";


    @GetMapping("/{deliveryId}")
    public ApiResponse<?> searchDeliveryById(@PathVariable Long deliveryId,
                                             @RequestHeader(name = USER_ID, required = false) String userId,
                                             @RequestHeader(name = USER_ROLE, required = false) String userRole) {
        DeliveryResponseDto data = deliveryService.searchDeliveryById(deliveryId, userId, userRole);
        return ApiResponse.success(data);
    }

    @GetMapping
    public ApiResponse<?> searchDeliveryList(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "createdAt") String sort,
                                             @RequestParam(defaultValue = "DESC") String direction,
                                             @RequestHeader(name = USER_ID, required = false) String userId,
                                             @RequestHeader(name = USER_ROLE, required = false) String userRole) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        List<DeliveryResponseDto> data = deliveryService.searchDeliveryList(pageable, userId, userRole);
        return ApiResponse.success(data);
    }

    @PutMapping("/{deliveryId}")
    public ApiResponse<?> updateDeliveryStatus(@PathVariable Long deliveryId,
                                               @RequestBody DeliveryUpdateDto deliveryStatusDto,
                                               @RequestHeader(name = USER_ID, required = false) String userId,
                                               @RequestHeader(name = USER_ROLE, required = false) String userRole) {
        DeliveryResponseDto data = deliveryService.updateDelivery(deliveryId, deliveryStatusDto, userId, userRole);
        return ApiResponse.success(data);
    }




}
