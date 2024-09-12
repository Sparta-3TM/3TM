package com.sparta3tm.orderserver.application.dto.request;

public record OrderRequestDto(Long productId,
                              Integer amount,
                              Long supplyCompanyId,
                              Long demandCompanyId,
                              Long deliveryId) {
}
