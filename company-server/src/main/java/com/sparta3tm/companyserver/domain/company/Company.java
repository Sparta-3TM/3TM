package com.sparta3tm.companyserver.domain.company;

import com.sparta3tm.common.BaseEntity;
import com.sparta3tm.companyserver.domain.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "company_type", nullable = false)
    private CompanyType companyType;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "company")
    private List<Product> products = new ArrayList<>();

    public void updateCompany(Long hubId, String name, String address, CompanyType companyType){
        this.hubId = hubId;
        this.name = name;
        this.address = address;
        this.companyType = companyType;
    }
}
