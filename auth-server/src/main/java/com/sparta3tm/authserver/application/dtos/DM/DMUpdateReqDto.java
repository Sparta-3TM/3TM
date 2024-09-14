package com.sparta3tm.authserver.application.dtos.DM;

import com.sparta3tm.authserver.domain.DM.ManagerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DMUpdateReqDto {
    private Long userId;
    private Long hubId;
    private String slackId;
    private ManagerType managerType;
}
