package com.sparta3tm.orderserver.domain.repository;

import com.sparta3tm.orderserver.domain.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
