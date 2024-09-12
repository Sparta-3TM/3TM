package com.sparta3tm.orderserver.application.dto.response.delivery;

import com.sparta3tm.orderserver.domain.entity.delivery.Delivery;

public record DeliveryResponseDto(Long deliveryId,
                                  String deliveryStatus,
                                  Long shipperId,
                                  Long startHub,
                                  Long endHub,
                                  String address,
                                  Long recipient,
                                  String recipientSlack) {

    public static DeliveryResponseDto of(Delivery delivery) {
        return new DeliveryResponseDto(delivery.getId(), delivery.getDeliveryStatus().toString(), delivery.getShipperId(), delivery.getStartHub(), delivery.getEndHub(), delivery.getAddress(), delivery.getRecipient(), delivery.getRecipientSlack());
    }

}
