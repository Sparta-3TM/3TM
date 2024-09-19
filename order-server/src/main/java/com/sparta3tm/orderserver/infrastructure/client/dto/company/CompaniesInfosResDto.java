package com.sparta3tm.orderserver.infrastructure.client.dto.company;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompaniesInfosResDto {
    private Long startHubId;
    private List<Long> supplyHubIds;
    private List<Long> demandHubIds;

}
