package com.sparta3tm.orderserver.infrastructure.client;

import com.sparta3tm.common.support.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "hub-server")
public interface HubClient {

    @PutMapping("{hmiId}/remove_startHub")
    ApiResponse<?> removeStartHub(@PathVariable Long hmiId, @RequestHeader("X-USER-ID") String username);


}
