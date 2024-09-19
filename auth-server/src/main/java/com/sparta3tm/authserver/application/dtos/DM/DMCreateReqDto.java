package com.sparta3tm.authserver.application.dtos.DM;

import com.sparta3tm.authserver.domain.DM.DeliveryManager;
import com.sparta3tm.authserver.domain.DM.ManagerType;
import com.sparta3tm.authserver.domain.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DMCreateReqDto {
    private Long userId;
    private Long hubId;
    private String slackId;
    private ManagerType managerType;

    public DeliveryManager toEntity(User user){
        return DeliveryManager.builder()
                .user(user)
                .hubId(this.hubId)
                .slackId(this.slackId)
                .managerType(this.managerType)
                .build();
    }
}
