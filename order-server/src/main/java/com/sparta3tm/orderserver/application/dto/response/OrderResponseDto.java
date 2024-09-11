package com.sparta3tm.orderserver.application.dto.response;

public record OrderResponseDto(Long id,
                               Long productId,
                               Integer amount,
                               Long supplyCompanyId,
                               Long demandCompanyId,
                               Long deliveryId) {
}
