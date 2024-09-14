package com.sparta3tm.orderserver.infrastructure.client.dto.hub;

import java.io.Serializable;
import java.time.LocalTime;

public record SubHMIDto(Long startHub,
                        Long endHub,
                        LocalTime estimatedTime,
                        Double estimatedDistance) implements Serializable {

}
