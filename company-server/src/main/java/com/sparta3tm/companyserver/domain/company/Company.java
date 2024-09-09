package com.sparta3tm.companyserver.domain.company;

import com.sparta3tm.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_delete is false")
@Entity(name = "p_companies")
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hub_id", nullable = false)
    private Long hubId;

    @Column(nullable = false)
    private String name;

    @Column(name = "company_type")
    private CompanyType companyType;

    @Column
    private String address;

    public void updateCompany(Long hubId, String name, String address, CompanyType companyType){
        this.hubId = hubId;
        this.name = name;
        this.address = address;
        this.companyType = companyType;
    }
}
