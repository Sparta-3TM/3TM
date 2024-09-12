package com.sparta3tm.orderserver.domain.entity.delivery;

import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;

public enum DeliveryStatus {
    WAITING_HUB, MOVING_HUB, IN_DELIVERY, ARRIVAL_HUB;

    public static DeliveryStatus fromString(String status) {
        try {
            return DeliveryStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new CoreApiException(ErrorType.BAD_REQUEST);
        }
    }

}
