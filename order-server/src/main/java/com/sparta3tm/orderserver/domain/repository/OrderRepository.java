package com.sparta3tm.orderserver.domain.repository;

import com.sparta3tm.orderserver.domain.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndIsDeleteFalse(Long orderId);

    Page<Order> findAllByUserIdAndIsDeleteFalse(Pageable pageable, String userId);
    Page<Order> findAllByIsDeleteFalse(Pageable pageable);

}
