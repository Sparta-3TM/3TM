package com.sparta3tm.orderserver.domain.repository;

import com.sparta3tm.orderserver.domain.entity.delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
