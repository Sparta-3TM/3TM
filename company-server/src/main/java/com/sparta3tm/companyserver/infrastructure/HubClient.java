package com.sparta3tm.companyserver.infrastructure;

import com.sparta3tm.common.support.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-server")
public interface HubClient {

    @GetMapping("/api/hubs/{hubId}")
    ApiResponse<?> searchHubById(@PathVariable Long hubId);
}
