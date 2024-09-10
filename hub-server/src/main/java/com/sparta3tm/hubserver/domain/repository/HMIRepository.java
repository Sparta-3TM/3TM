package com.sparta3tm.hubserver.domain.repository;

import com.sparta3tm.hubserver.domain.entity.HubMovementInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HMIRepository extends JpaRepository<HubMovementInfo, Long> {

    Optional<HubMovementInfo> findByIdAndIsDeleteFalse(Long id);
    Page<HubMovementInfo> findAllByIsDeleteFalse(Pageable pageable);
}
