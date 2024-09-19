package com.sparta3tm.hubserver.application.dto.hub.response;

public record ResponseHubManagerDto(String managerId) {

    public static ResponseHubManagerDto of(String userId) {
        return new ResponseHubManagerDto(userId);
    }

}
