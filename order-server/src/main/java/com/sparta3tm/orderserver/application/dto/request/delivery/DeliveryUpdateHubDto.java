package com.sparta3tm.orderserver.application.dto.request.delivery;

import java.time.LocalTime;

public record DeliveryUpdateHubDto(String deliveryStatus,
                                   Long startHub,
                                   Long endHub,
                                   String address,
                                   LocalTime duration,
                                   Double distance) {

}
