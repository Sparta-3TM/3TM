package com.sparta3tm.orderserver.application.dto.request.order;

public record OrderRequestDto(Long productId,
                              Integer amount,
                              Long supplyCompanyId,
                              Long demandCompanyId) {
}
