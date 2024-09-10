package com.sparta3tm.hubserver.application.dto.hmi;

import com.sparta3tm.hubserver.domain.entity.HubMovementInfo;

import java.time.LocalTime;

public record SubHMIDto(Long startHub,
                        Long endHub,
                        LocalTime estimatedTime,
                        Double estimatedDistance) {

    public static SubHMIDto of(HubMovementInfo hubMovementInfo) {
        return new SubHMIDto(hubMovementInfo.getStartHub(), hubMovementInfo.getEndHub(), hubMovementInfo.getEstimatedTime(), hubMovementInfo.getEstimatedDistance());
    }
}
