package com.sparta3tm.hubserver.application.dto.hmi;

import com.sparta3tm.hubserver.domain.entity.HubMovementInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

@Slf4j
public record ResponseHMIDto(Long id,
                             Long startHub,
                             Long endHub,
                             String address,
                             LocalTime estimatedTime,
                             Double estimatedDistance,
                             List<SubHMIDto> subRoot) implements Serializable {

    public static ResponseHMIDto of(HubMovementInfo hubMovementInfo) {
        List<SubHMIDto> list = hubMovementInfo.getSubMovementInfo().stream().map(SubHMIDto::of).toList();
        return new ResponseHMIDto(hubMovementInfo.getId(), hubMovementInfo.getStartHub(), hubMovementInfo.getEndHub(), hubMovementInfo.getStartHubAddress(), hubMovementInfo.getEstimatedTime(), hubMovementInfo.getEstimatedDistance(), list);
    }
}
