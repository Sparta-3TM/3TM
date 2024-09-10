package com.sparta3tm.companyserver.application.dtos.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateReqDto {
    @NotNull
    private Long companyId;
    @NotNull
    private Long hubId;
    @NotNull
    private String productName;
    @NotNull
    private Integer quantity;



}
