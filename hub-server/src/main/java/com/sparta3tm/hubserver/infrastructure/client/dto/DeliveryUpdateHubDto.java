package com.sparta3tm.hubserver.infrastructure.client.dto;

import java.time.LocalTime;

public record DeliveryUpdateHubDto(String deliveryStatus,
                                   Long startHub,
                                   Long endHub,
                                   String address,
                                   LocalTime duration,
                                   Double distance) {

}
