package com.sparta3tm.orderserver.infrastructure.client.dto.hub;

import java.time.LocalTime;
import java.util.List;

public record RequestHMIDto(Long startHub,
                            Long endHub,
                            List<Long> transitHubId) {

}

