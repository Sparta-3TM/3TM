package com.sparta3tm.orderserver.application.dto.response.order;


import java.util.List;

public record OrderListResponseDto(List<OrderResponseDto> orderRequestDtoList, Long managerId) {
}
