package com.sparta3tm.hubserver.infrastructure.client;

import com.sparta3tm.common.support.response.ApiResponse;
import com.sparta3tm.hubserver.infrastructure.client.dto.DeliveryUpdateHubDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "order-server")
public interface OrderClient {

    @PatchMapping("/api/deliveries/update_hmi/{hmiId}")
    ApiResponse<?> updateDeliveryByHmi(@PathVariable Long hmiId,
                                       @RequestBody DeliveryUpdateHubDto deliveryUpdateHubDto,
                                       @RequestHeader(name = "X-USER-ID", required = false) String userId,
                                       @RequestHeader(name = "X-USER-ROLE", required = false) String userRole);
}