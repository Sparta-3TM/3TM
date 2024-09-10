package com.sparta3tm.companyserver.application.dtos.company;

import com.sparta3tm.companyserver.domain.company.Company;
import com.sparta3tm.companyserver.domain.company.CompanyType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreateReqDto {
    @NotNull
    private Long hubId;
    @NotNull
    private String name;
    @NotNull
    private CompanyType companyType;
    @NotNull
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
