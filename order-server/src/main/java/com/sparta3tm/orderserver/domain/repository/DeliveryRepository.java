package com.sparta3tm.orderserver.domain.repository;

import com.sparta3tm.orderserver.domain.entity.delivery.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByIdAndIsDeleteFalse(Long id);

    Optional<Delivery> findByRecipientSlack(String userId);

    Page<Delivery> findAllByIsDeleteFalse(Pageable pageable);
}
