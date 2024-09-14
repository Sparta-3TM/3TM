package com.sparta3tm.orderserver.infrastructure.client.dto.hub;

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


}
