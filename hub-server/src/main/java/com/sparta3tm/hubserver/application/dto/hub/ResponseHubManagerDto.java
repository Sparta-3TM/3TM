package com.sparta3tm.hubserver.application.dto.hub;

public record ResponseHubManagerDto(String managerId) {

    public static ResponseHubManagerDto of(String userId) {
        return new ResponseHubManagerDto(userId);
    }

}
