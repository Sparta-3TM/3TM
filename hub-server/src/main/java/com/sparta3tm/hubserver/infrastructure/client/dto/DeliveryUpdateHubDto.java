package com.sparta3tm.hubserver.infrastructure.client.dto;

public record DeliveryUpdateHubDto(String deliveryStatus,
                                   Long startHub,
                                   Long endHub,
                                   String address) {

}
