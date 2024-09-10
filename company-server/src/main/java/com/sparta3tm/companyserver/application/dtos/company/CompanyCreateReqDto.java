package com.sparta3tm.companyserver.application.dtos.company;

import com.sparta3tm.companyserver.domain.company.Company;
import com.sparta3tm.companyserver.domain.company.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreateReqDto {
    private Long hubId;
    private String name;
    private CompanyType companyType;
    private String address;

    public Company toEntity(){
        return Company.builder()
                .hubId(this.hubId)
                .name(this.name)
                .companyType(this.companyType)
                .address(this.address)
                .build();
    }
}
