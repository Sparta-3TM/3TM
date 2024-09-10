package com.sparta3tm.companyserver.application;

import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.companyserver.application.dtos.company.CompanyCreateReqDto;
import com.sparta3tm.companyserver.application.dtos.company.CompanyResDto;
import com.sparta3tm.companyserver.application.dtos.company.CompanyUpdateReqDto;
import com.sparta3tm.companyserver.domain.company.Company;
import com.sparta3tm.companyserver.domain.company.CompanyRepository;
import com.sparta3tm.companyserver.domain.company.CompanyType;
import com.sparta3tm.companyserver.infrastructure.HubClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public CompanyResDto updateCompany(Long id, CompanyUpdateReqDto updateReqDto){
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

        try{
            company.updateCompany(updateReqDto.getHubId(), updateReqDto.getName(), updateReqDto.getAddress(), updateReqDto.getCompanyType());
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        return CompanyResDto.fromCompany(company);
    }

    @Transactional
    public String deleteCompany(Long id, String userId){
        Company company = companyRepository.findById(id).orElseThrow(()->{
            log.error("존재하지 않는 Company ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        });

        try{
            company.softDelete(userId);
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        return "Company " + company.getId() + " is Deleted";
    }
}
