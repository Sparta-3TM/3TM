package com.sparta3tm.hubserver.application.dto;

import com.sparta3tm.hubserver.domain.entity.Hub;

public record ResponseHubDto(Long id,
                             String hubName,
                             String address,
                             Double latitude,
                             Double longitude) {

    public static ResponseHubDto of(Hub hub) {
        return new ResponseHubDto(hub.getId(), hub.getName(), hub.getAddress(), hub.getLatitude(), hub.getLongitude());
    }

}
