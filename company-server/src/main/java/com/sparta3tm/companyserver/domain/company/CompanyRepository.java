package com.sparta3tm.companyserver.domain.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Page<Company> findAllByNameContaining(String keyword, Pageable pageable);
    Page<Company> findAllByCompanyTypeAndNameContaining(CompanyType companyType, String keyword, Pageable pageable);
}
