package com.sparta3tm.companyserver.application.dtos.product;

import com.sparta3tm.companyserver.domain.company.Company;
import com.sparta3tm.companyserver.domain.product.Product;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateReqDto {
    @NotNull
    private Long companyId;
    @NotNull
    private Long hubId;
    @NotNull
    private String productName;
    private String productDescription;
    @NotNull
    private Integer quantity;

    public Product toEntity(Company company){
        return Product.builder()
                .company(company)
                .hubId(this.hubId)
                .productName(this.productName)
                .productDescription(this.productDescription)
                .quantity(this.quantity)
                .build();
    }
}
