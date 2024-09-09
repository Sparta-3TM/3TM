package com.sparta3tm.hubserver.application.dto;

import java.util.List;

public record ResponsePageHubDto(List<ResponseHubDto> responseHubDtoList,
                                 Boolean hasNext) {

    public static ResponsePageHubDto of(List<ResponseHubDto> responseHubDto, Boolean hasNext) {
        return new ResponsePageHubDto(responseHubDto, hasNext);
    }
}
