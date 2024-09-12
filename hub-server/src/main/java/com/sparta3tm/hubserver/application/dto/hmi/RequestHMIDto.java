package com.sparta3tm.hubserver.application.dto.hmi;

import java.time.LocalTime;
import java.util.List;

public record RequestHMIDto(Long startHub,
                            Long endHub,
                            LocalTime estimatedTime,
                            Double estimatedDistance,
                            List<Long> transitHubId) {

}
