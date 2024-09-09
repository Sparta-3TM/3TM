package com.sparta3tm.companyserver.controller;

import com.sparta3tm.common.support.response.ApiResponse;
import com.sparta3tm.companyserver.application.CompanyService;
import com.sparta3tm.companyserver.application.dtos.CompanyCreateReqDto;
import com.sparta3tm.companyserver.application.dtos.CompanyUpdateReqDto;
import com.sparta3tm.companyserver.domain.company.CompanyType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Company API")
@Slf4j(topic = "CompanyController")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "Company Create")
    @PostMapping
    public ApiResponse<?> createCompany(
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @RequestBody CompanyCreateReqDto companyCreateReqDto) {
        return ApiResponse.success(companyService.createCompany(companyCreateReqDto));
    }

    @Operation(summary = "Company Get One By ID")
    @GetMapping("/{companyId}")
    public ApiResponse<?> getCompanyById(
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @PathVariable(name = "companyId") Long companyId){
        return ApiResponse.success(companyService.getCompany(companyId));
    }

    @Operation(summary = "Company Search")
    @GetMapping
    public ApiResponse<?> searchCompany(
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "createdAt", name = "sort") String sort,
            @RequestParam(defaultValue = "DESC", name = "direction") String direction,
            @RequestParam(required = false, name = "keyword") String keyword,
            @RequestParam(required = false, name = "companyType") CompanyType companyType
            ){
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return ApiResponse.success(companyService.searchCompany(keyword, companyType, pageable));
    }

    @Operation(summary = "Company Update")
    @PutMapping("/{companyId}")
    public ApiResponse<?> updateCompany(
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @PathVariable(name = "companyId") Long companyId,
            @RequestBody CompanyUpdateReqDto companyUpdateReqDto
    ){
        return ApiResponse.success(companyService.updateCompany(companyId, companyUpdateReqDto));
    }

    @Operation(summary = "Company Delete")
    @DeleteMapping("/{companyId}")
    public ApiResponse<?> deleteCompany(
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @PathVariable(name = "companyId") Long companyId
    ){
        return ApiResponse.success(companyService.deleteCompany(companyId, userId));
    }

}
