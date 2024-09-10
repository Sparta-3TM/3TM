package com.sparta3tm.hubserver.application.dto.hmi;

import java.util.List;

public record ResponsePageHMIDto(List<ResponseHMIDto> responseHMIDtoList, boolean hasNext) {

    public static ResponsePageHMIDto of(List<ResponseHMIDto> responseHMIDtoList, boolean hasNext) {
        return new ResponsePageHMIDto(responseHMIDtoList, hasNext);
    }

}
