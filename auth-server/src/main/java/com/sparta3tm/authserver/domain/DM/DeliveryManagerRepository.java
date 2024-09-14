package com.sparta3tm.authserver.domain.DM;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, Long> {

    Page<DeliveryManager> findAllBySlackIdContaining(String keyword, Pageable pageable);
}
