package com.sparta3tm.hubserver.application.dto.hub;

import com.sparta3tm.hubserver.domain.entity.Hub;

import java.io.Serializable;

public record ResponseHubDto(Long id,
                             String hubName,
                             String address,
                             Double latitude,
                             Double longitude,
                             String managerId) implements Serializable {

    public static ResponseHubDto of(Hub hub) {
        return new ResponseHubDto(hub.getId(), hub.getName(), hub.getAddress(), hub.getLatitude(), hub.getLongitude(), hub.getManagerId());
    }

}
