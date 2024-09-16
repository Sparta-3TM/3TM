package com.sparta3tm.orderserver.application.dto.request.delivery;

import jakarta.persistence.Column;

public record DeliveryUpdateDto(String deliveryStatus,
                                String recipientSlack) {




}
