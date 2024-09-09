package com.sparta3tm.companyserver.application.dtos;

import com.sparta3tm.companyserver.domain.company.Company;
import com.sparta3tm.companyserver.domain.company.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResDto {
    private Long id;
    private Long hubId;
    private String name;
    private CompanyType companyType;
    private String address;

    public static CompanyResDto fromCompany(Company company) {
        return CompanyResDto.builder()
                .id(company.getId())
                .hubId(company.getHubId())
                .name(company.getName())
                .companyType(company.getCompanyType())
                .address(company.getAddress())
                .build();
    }
}
