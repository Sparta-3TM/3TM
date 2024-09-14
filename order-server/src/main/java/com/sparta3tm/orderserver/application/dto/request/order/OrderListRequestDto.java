package com.sparta3tm.orderserver.application.dto.request.order;

import java.util.List;

public record OrderListRequestDto(List<OrderRequestDto> orderDtoList, Long shipperId) {
}
