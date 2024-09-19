package com.sparta3tm.companyserver.application;

import com.sparta3tm.common.gemini.GeminiReqDto;
import com.sparta3tm.common.gemini.GeminiResDto;
import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.companyserver.application.dtos.product.*;
import com.sparta3tm.companyserver.domain.company.Company;
import com.sparta3tm.companyserver.domain.company.CompanyRepository;
import com.sparta3tm.companyserver.domain.product.Product;
import com.sparta3tm.companyserver.domain.product.ProductRepository;
import com.sparta3tm.companyserver.infrastructure.HubClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j(topic = "ProductService")
@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final HubClient hubClient;
    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    String geminiApiKey;


    @Transactional
    public ProductResDto createProduct(ProductCreateReqDto createReqDto) {
        try{
            hubClient.searchHubById(createReqDto.getHubId());
        }catch(FeignException e){
            log.error("존재하지 않는 Hub ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }

        try{
            Company company = companyRepository.findById(createReqDto.getCompanyId()).orElseThrow();
            return ProductResDto.from(productRepository.save(createReqDto.toEntity(company)));
        }catch (Exception e){
            log.error("존재하지 않는 Company ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }
    }

    public ProductResDto getProduct(Long productId) {
        return ProductResDto.from(productRepository.findById(productId).orElseThrow(()->{
            log.error("존재하지 않는 Product ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }));
    }

    public List<ProductResDto> searchProduct(String keyword, Pageable pageable) {
        try{
            if(keyword == null){
                return productRepository.findAllByProductNameContaining("", pageable).stream().map(ProductResDto::from).toList();
            }
            return productRepository.findAllByProductNameContaining(keyword, pageable).stream().map(ProductResDto::from).toList();
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }
    }

    @Transactional
    public ProductResDto updateProduct(String userRole, String userId, Long productId, ProductUpdateReqDto productUpdateReqDto) {
        try{
            hubClient.searchHubById(productUpdateReqDto.getHubId());
        }catch(FeignException e){
            log.error("존재하지 않는 Hub ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }

        Company company;
        try{
            company = companyRepository.findById(productUpdateReqDto.getCompanyId()).orElseThrow();
        }catch (Exception e){
            log.error("존재하지 않는 Company ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }

        Product product = productRepository.findById(productId).orElseThrow(()->{
            log.error("존재하지 않는 Product ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        });

        if("COMPANY".equals(userRole) && !product.getCreatedBy().equals(userId)){
            log.error("본인의 상품 외에는 관리할 수 없습니다.");
            throw new CoreApiException(ErrorType.FORBIDDEN);
        }

        try{
            product.updateProductInfo(company, productUpdateReqDto.getHubId(),
                    productUpdateReqDto.getProductName(), productUpdateReqDto.getProductDescription(), productUpdateReqDto.getQuantity());
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        return ProductResDto.from(product);
    }

    @Transactional
    public String updateProductsQuantities(ProductsUpdateQuantitiesReqDto productsUpdateQuantitiesReqDto) {
        List<Product> products = new ArrayList<>();
        for(Long productId : productsUpdateQuantitiesReqDto.getProductIds()){
            products.add(
                    productRepository.findById(productId).orElseThrow(()->{
                        log.error("존재하지 않는 Product ID 입니다.");
                        throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
                    })
            );
        }

        for(int i=0;i<products.size();i++){
            products.get(i).controlQuantity(productsUpdateQuantitiesReqDto.getQuantityList().get(i));
            if(products.get(i).getQuantity()<0){
                log.error("수량은 0보다 작을 수 없습니다.");
                throw new CoreApiException(ErrorType.BAD_REQUEST);
            }
        }

        return "Products' quantities are modified";
    }

    @Transactional
    public String deleteProduct(String userRole, String userId, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->{
            log.error("존재하지 않는 Product ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        });

        if("COMPANY".equals(userRole) && !product.getCreatedBy().equals(userId)){
            log.error("본인의 상품 외에는 관리할 수 없습니다.");
            throw new CoreApiException(ErrorType.FORBIDDEN);
        }

        try{
            product.softDelete(userId);
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        return "Product " + product.getId() + " is Deleted";
    }

    @Cacheable(value = "productDescription", key = "args[0]")
    public ProductDescriptionResDto getAIDescription(String productName){
        String geminiURL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key="
                + geminiApiKey;
        String requestText = productName + "에 대한 상품 설명을 50자 이내로 작성해줘.";
        GeminiReqDto request = new GeminiReqDto();
        request.createGeminiReqDto(requestText);
        String description = "";
        try{
            GeminiResDto response = restTemplate.postForObject(geminiURL, request, GeminiResDto.class);
            description = response.getCandidates().get(0).getContent().getParts().get(0).getText();
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        return new ProductDescriptionResDto(description);
    }
}
