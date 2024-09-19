package com.sparta3tm.orderserver.domain.repository;

import com.sparta3tm.orderserver.domain.entity.delivery_route.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, Long> {

    Optional<DeliveryRoute> findByHmiIdAndIsDeleteFalse(Long hmiId);
}
