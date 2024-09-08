package com.sparta3tm.hubserver.domain.repository;

import com.sparta3tm.hubserver.domain.entity.HubMovementInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubMovementInfoRepository extends JpaRepository<HubMovementInfo, Long> {
}
