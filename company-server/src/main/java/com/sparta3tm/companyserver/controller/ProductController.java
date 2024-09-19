package com.sparta3tm.companyserver.controller;

import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.common.support.response.ApiResponse;
import com.sparta3tm.companyserver.application.ProductService;
import com.sparta3tm.companyserver.application.dtos.product.ProductCreateReqDto;
import com.sparta3tm.companyserver.application.dtos.product.ProductUpdateReqDto;
import com.sparta3tm.companyserver.application.dtos.product.ProductsUpdateQuantitiesReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product API")
@Slf4j(topic = "ProductController")
public class ProductController {
    
    private final ProductService productService;

    @Operation(summary = "Product Create")
    @PostMapping
    public ApiResponse<?> createProduct(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @RequestBody ProductCreateReqDto productCreateReqDto) {
        if("MASTER".equals(userRole) || "COMPANY".equals(userRole)) {
            return ApiResponse.success(productService.createProduct(productCreateReqDto));
        }else
            return ApiResponse.error(ErrorType.FORBIDDEN);
    }

    @Operation(summary = "Product Get One By ID")
    @GetMapping("/{productId}")
    public ApiResponse<?> getProductById(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @PathVariable(name = "productId") Long productId){
        return ApiResponse.success(productService.getProduct(productId));
    }

    @Operation(summary = "Product Search")
    @GetMapping
    public ApiResponse<?> searchProduct(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "createdAt", name = "sort") String sort,
            @RequestParam(defaultValue = "DESC", name = "direction") String direction,
            @RequestParam(required = false, name = "keyword") String keyword
    ){
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return ApiResponse.success(productService.searchProduct(keyword, pageable));
    }

    @Operation(summary = "Product Update")
    @PutMapping("/{productId}")
    public ApiResponse<?> updateProduct(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @PathVariable(name = "productId") Long productId,
            @RequestBody ProductUpdateReqDto productUpdateReqDto
    ){
        if("SHIPPER".equals(userRole))
            return ApiResponse.error(ErrorType.FORBIDDEN);
        else
            return ApiResponse.success(productService.updateProduct(userRole, userId, productId, productUpdateReqDto));
    }

    @Operation(summary = "Products Update Quantities")
    @PatchMapping
    public ApiResponse<?> updateProduct(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @RequestBody ProductsUpdateQuantitiesReqDto productsUpdateQuantitiesReqDto
    ){
        return ApiResponse.success(productService.updateProductsQuantities(productsUpdateQuantitiesReqDto));
    }

    @Operation(summary = "Product Delete")
    @DeleteMapping("/{productId}")
    public ApiResponse<?> deleteProduct(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @PathVariable(name = "productId") Long productId
    ){
        if("SHIPPER".equals(userRole))
            return ApiResponse.error(ErrorType.FORBIDDEN);
        else
            return ApiResponse.success(productService.deleteProduct(userRole, userId, productId));
    }

    @Operation(summary = "Product AI Description")
    @GetMapping("/description")
    public ApiResponse<?> getAIDescription(
            @RequestHeader(name = "X-USER-ROLE", required = false) String userRole,
            @RequestHeader(name = "X-USER-ID", required = false) String userId,
            @RequestParam(name = "productName") String productName
    ){
        if("SHIPPER".equals(userRole))
            return ApiResponse.error(ErrorType.FORBIDDEN);
        else
            return ApiResponse.success(productService.getAIDescription(productName));
    }
}
