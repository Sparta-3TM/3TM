package com.sparta3tm.orderserver.infrastructure.client;

import com.sparta3tm.common.support.response.ApiResponse;
import com.sparta3tm.orderserver.infrastructure.client.dto.hub.RequestHMIDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "hub-server")
public interface HubClient {

    @PutMapping("/api/hub_movement_infos/{hmiId}/remove_startHub")
    ApiResponse<?> removeStartHub(@PathVariable Long hmiId, @RequestHeader("X-USER-ID") String username);


    @GetMapping("/api/hub_movement_infos/{hmiId}/manager")
    ApiResponse<?> searchHmiManager(@PathVariable Long hmiId,
                                           @RequestHeader("X-USER-ID") String userId,
                                           @RequestHeader("X-USER-ROLE") String userRole);

    @PostMapping
    public ApiResponse<?> createHmi(@RequestBody RequestHMIDto requestHMIDto,
                                    @RequestHeader("X-USER-ID") String userId,
                                    @RequestHeader("X-USER-ROLE") String userRole);

}
