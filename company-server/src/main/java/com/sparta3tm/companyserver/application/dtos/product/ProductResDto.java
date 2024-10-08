package com.sparta3tm.companyserver.application.dtos.product;

import com.sparta3tm.companyserver.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResDto {
    private Long id;
    private Long companyId;
    private Long hubId;
    private String productName;
    private String productDescription;
    private Integer quantity;

    public static ProductResDto from(Product product) {
        return ProductResDto.builder()
                .id(product.getId())
                .companyId(product.getCompany().getId())
                .hubId(product.getHubId())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .quantity(product.getQuantity())
                .build();
    }
}
