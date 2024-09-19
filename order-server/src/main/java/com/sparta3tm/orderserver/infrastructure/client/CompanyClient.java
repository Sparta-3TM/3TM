package com.sparta3tm.orderserver.infrastructure.client;

import com.sparta3tm.common.support.response.ApiResponse;
import com.sparta3tm.orderserver.infrastructure.client.dto.company.CompaniesInfosReqDto;
import com.sparta3tm.orderserver.infrastructure.client.dto.company.ProductsUpdateQuantitiesReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "company-server", url = "http://localhost:19094")
public interface CompanyClient {

    @PatchMapping("/api/products")
    ApiResponse<?> updateProduct(@RequestHeader(name = "X-USER-ID", required = false) String userId,
                                 @RequestBody ProductsUpdateQuantitiesReqDto productsUpdateQuantitiesReqDto);

    @GetMapping("/api/companies/{companyId}")
    ApiResponse<?> getCompanyById(@RequestHeader(name = "X-USER-ID", required = false) String userId,
                                  @PathVariable(name = "companyId") Long companyId);

    @PostMapping("/api/companies/hubIds")
    ApiResponse<?> getCompanyById(@RequestHeader(name = "X-USER-ID", required = false) String userId,
                                  @RequestBody CompaniesInfosReqDto companiesInfosReqDto);
}