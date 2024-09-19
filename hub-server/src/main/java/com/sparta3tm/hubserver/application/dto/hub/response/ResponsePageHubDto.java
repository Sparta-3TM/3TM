package com.sparta3tm.hubserver.application.dto.hub.response;

import java.io.Serializable;
import java.util.List;

public record ResponsePageHubDto(List<ResponseHubDto> responseHubDtoList, Boolean hasNext) implements Serializable {

    public static ResponsePageHubDto of(List<ResponseHubDto> responseHubDto, Boolean hasNext) {
        return new ResponsePageHubDto(responseHubDto, hasNext);
    }
}
