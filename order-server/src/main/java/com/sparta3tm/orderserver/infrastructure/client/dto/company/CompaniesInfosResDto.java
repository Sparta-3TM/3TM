package com.sparta3tm.orderserver.infrastructure.client.dto.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompaniesInfosResDto {
    private Long startHubId;
    private List<Long> supplyHubIds;
    private List<Long> demandHubIds;

    public static CompaniesInfosResDto of (Long startHubId, List<Long> supplyHubIds, List<Long> demandHubIds){
        return new CompaniesInfosResDto(startHubId, supplyHubIds, demandHubIds);
    }
}
