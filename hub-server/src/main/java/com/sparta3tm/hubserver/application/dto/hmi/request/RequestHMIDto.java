package com.sparta3tm.hubserver.application.dto.hmi.request;

import java.time.LocalTime;
import java.util.List;

public record RequestHMIDto(Long startHub,
                            Long endHub,
                            List<Long> transitHubId) {

}
