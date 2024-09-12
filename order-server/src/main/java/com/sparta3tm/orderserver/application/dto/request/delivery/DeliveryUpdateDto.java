package com.sparta3tm.orderserver.application.dto.request.delivery;

public record DeliveryUpdateDto(String deliveryStatus,
                                String address,
                                String recipientSlack) {


}
