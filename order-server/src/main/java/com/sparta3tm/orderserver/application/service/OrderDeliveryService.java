package com.sparta3tm.orderserver.application.service;

import com.sparta3tm.orderserver.application.dto.request.OrderRequestDto;
import com.sparta3tm.orderserver.domain.entity.delivery.Delivery;
import com.sparta3tm.orderserver.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderDeliveryService {

    private final OrderRepository orderRepository;

    @Transactional
    public void createOrder(OrderRequestDto orderRequestDto, String userId) {

//        new Delivery()
//        orderRequestDto.
    }
}
