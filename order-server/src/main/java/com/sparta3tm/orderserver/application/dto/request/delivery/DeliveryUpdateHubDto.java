package com.sparta3tm.orderserver.application.dto.request.delivery;

public record DeliveryUpdateHubDto(String deliveryStatus,
                                   Long startHub,
                                   Long endHub,
                                   String address) {

}
