package com.sparta3tm.orderserver.infrastructure.client.dto.company;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductsUpdateQuantitiesReqDto {
    @NotNull
    private List<Long> productIds;
    @NotNull
    private List<Integer> quantityList;
}
