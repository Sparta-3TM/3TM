package com.sparta3tm.companyserver.application.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompaniesInfosReqDto {
    private List<Long> supplyIds;
    private List<Long> demandIds;
}
