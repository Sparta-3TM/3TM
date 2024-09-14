package com.sparta3tm.authserver.application.dtos.DM;

import com.sparta3tm.authserver.domain.DM.DeliveryManager;
import com.sparta3tm.authserver.domain.DM.ManagerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DMResDto {
    private Long id;
    private Long userId;
    private Long hubId;
    private String slackId;
    private ManagerType managerType;

    public static DMResDto from(DeliveryManager deliveryManager){
        return DMResDto.builder()
                .id(deliveryManager.getId())
                .userId(deliveryManager.getUser().getId())
                .hubId(deliveryManager.getHubId())
                .slackId(deliveryManager.getSlackId())
                .managerType(deliveryManager.getManagerType())
                .build();
    }
}
