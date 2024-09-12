package com.sparta3tm.orderserver.application.dto.response.order;

import com.sparta3tm.orderserver.domain.entity.order.Order;

public record OrderResponseDto(Long orderId,
                               String userId,
                               Long productId,
                               Integer amount,
                               Long supplyCompanyId,
                               Long demandCompanyId) {

    public static OrderResponseDto of(Order order) {
        return new OrderResponseDto(order.getId(), order.getUserId(), order.getProductId(), order.getAmount(), order.getSupplyCompanyId(), order.getDemandCompanyId());
    }
}
