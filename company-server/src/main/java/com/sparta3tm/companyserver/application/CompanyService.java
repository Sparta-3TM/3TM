package com.sparta3tm.companyserver.application;

import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.companyserver.application.dtos.company.*;
import com.sparta3tm.companyserver.domain.company.Company;
import com.sparta3tm.companyserver.domain.company.CompanyRepository;
import com.sparta3tm.companyserver.domain.company.CompanyType;
import com.sparta3tm.companyserver.domain.product.Product;
import com.sparta3tm.companyserver.infrastructure.HubClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j(topic = "CompanyService")
@Service
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final HubClient hubClient;

    @Transactional
    public CompanyResDto createCompany(CompanyCreateReqDto createReqDto){
        try{
            hubClient.searchHubById(createReqDto.getHubId());
        }catch(FeignException e){
            log.error("존재하지 않는 Hub ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }

        return CompanyResDto.fromCompany(companyRepository.save(createReqDto.toEntity()));
    }

    public CompanyResDto getCompany(Long id){
        return CompanyResDto.fromCompany(companyRepository.findById(id).orElseThrow(()->{
            log.error("존재하지 않는 Company ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }));
    }

    public List<CompanyResDto> searchCompany(String keyword, CompanyType companyType, Pageable pageable) {
        try {
            if (keyword == null) {
                if (companyType != null) {
                    return companyRepository.findAllByCompanyTypeAndNameContaining(companyType, "", pageable).stream().map(CompanyResDto::fromCompany).toList();
                }
                return companyRepository.findAllByNameContaining("", pageable).stream().map(CompanyResDto::fromCompany).toList();
            } else {
                if (companyType != null) {
                    return companyRepository.findAllByCompanyTypeAndNameContaining(companyType, keyword, pageable).stream().map(CompanyResDto::fromCompany).toList();
                }
                return companyRepository.findAllByNameContaining(keyword, pageable).stream().map(CompanyResDto::fromCompany).toList();
            }
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }
    }

    public CompaniesInfosResDto getCompaniesHubIds(CompaniesInfosReqDto companiesInfosReqDto){
        Long starthub = 1L;
        List<Long> supplyHubIds = new ArrayList<>();

        boolean isFirst = true;
        for(Long id : companiesInfosReqDto.getSupplyIds()){
            Company company = companyRepository.findById(id).orElseThrow(()->{
                log.error("존재하지 않는 Company ID 입니다.");
                throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
            });
            if(isFirst){
                starthub = company.getHubId();
                isFirst = false;
            }
            supplyHubIds.add(company.getHubId());
        }

        List<Long> demandHubIds = new ArrayList<>();
        for(Long id : companiesInfosReqDto.getDemandIds()){
            Company company = companyRepository.findById(id).orElseThrow(()->{
                log.error("존재하지 않는 Company ID 입니다.");
                throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
            });
            demandHubIds.add(company.getHubId());
        }

        return CompaniesInfosResDto.of(starthub, supplyHubIds, demandHubIds);
    }

    @Transactional
    public CompanyResDto updateCompany(String userRole, String userId, Long id, CompanyUpdateReqDto updateReqDto){
        try{
            hubClient.searchHubById(updateReqDto.getHubId());
        }catch(FeignException e){
            log.error("존재하지 않는 Hub ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }

        Company company = companyRepository.findById(id).orElseThrow(()->{
            log.error("존재하지 않는 Company ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        });

        if("COMPANY".equals(userRole) && !company.getCreatedBy().equals(userId)){
            log.error("본인의 업체 외에는 관리할 수 없습니다.");
            throw new CoreApiException(ErrorType.FORBIDDEN);
        }

        try{
            company.updateCompany(updateReqDto.getHubId(), updateReqDto.getName(), updateReqDto.getAddress(), updateReqDto.getCompanyType());
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        return CompanyResDto.fromCompany(company);
    }

    @Transactional
    public String deleteCompany(String userRole, String userId, Long id){
        Company company = companyRepository.findById(id).orElseThrow(()->{
            log.error("존재하지 않는 Company ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        });

        if("COMPANY".equals(userRole) && !company.getCreatedBy().equals(userId)){
            log.error("본인의 업체 외에는 관리할 수 없습니다.");
            throw new CoreApiException(ErrorType.FORBIDDEN);
        }

        try{
            for(Product product : company.getProducts()){
                product.softDelete(userId);
            }
            company.softDelete(userId);
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        return "Company " + company.getId() + " is Deleted";
    }
}
