package com.sparta3tm.orderserver.application.service;

import com.sparta3tm.common.support.RoleCheck;
import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.orderserver.application.dto.response.order.OrderResponseDto;
import com.sparta3tm.orderserver.domain.entity.order.Order;
import com.sparta3tm.orderserver.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RoleCheck roleCheck = new RoleCheck();

    @Transactional(readOnly = true)
    public OrderResponseDto searchOrderById(Long orderId, String userId, String userRole) {
        // 마스터는 그냥 조회 , 사용자는 자신의 주문만 조회
        Order order = orderRepository.findByIdAndIsDeleteFalse(orderId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        if (!roleCheck.CHECK_MASTER(userRole))
            if (!order.getUserId().equals(userId)) throw new CoreApiException(ErrorType.BAD_REQUEST);
        return OrderResponseDto.of(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> searchOrderList(Pageable pageable, String userId, String userRole) {
        // 마스터는 모든 주문 조회 , 사용자는 자신의 주문만 조회
        if (roleCheck.CHECK_MASTER(userRole)) return orderRepository.findAllByIsDeleteFalse(pageable).stream().map(OrderResponseDto::of).toList();
        else return orderRepository.findAllByUserIdAndIsDeleteFalse(pageable, userId).stream().map(OrderResponseDto::of).toList();
    }

}
