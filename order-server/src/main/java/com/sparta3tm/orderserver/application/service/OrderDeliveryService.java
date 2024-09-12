package com.sparta3tm.orderserver.application.service;

import com.sparta3tm.common.support.RoleCheck;
import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.orderserver.application.dto.request.order.OrderRequestDto;
import com.sparta3tm.orderserver.application.dto.request.order.OrderListRequestDto;
import com.sparta3tm.orderserver.application.dto.request.order.UpdateAmountOrderDto;
import com.sparta3tm.orderserver.application.dto.response.order.OrderResponseDto;
import com.sparta3tm.orderserver.domain.entity.delivery.Delivery;
import com.sparta3tm.orderserver.domain.entity.delivery.DeliveryStatus;
import com.sparta3tm.orderserver.domain.entity.delivery_route.DeliveryRoute;
import com.sparta3tm.orderserver.domain.entity.order.Order;
import com.sparta3tm.orderserver.domain.repository.DeliveryRepository;
import com.sparta3tm.orderserver.domain.repository.OrderRepository;
import com.sparta3tm.orderserver.infrastructure.client.CompanyClient;
import com.sparta3tm.orderserver.infrastructure.client.dto.company.ProductsUpdateQuantitiesReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final CompanyClient companyClient;
    private final RoleCheck roleCheck = new RoleCheck();

    @Transactional
    public void createOrder(OrderListRequestDto orderRequestDto, String userId) {
        // 모두 생성 가능
        List<OrderRequestDto> orderDtoList = orderRequestDto.orderDtoList();
        Long managerId = orderRequestDto.managerId();
        List<Long> supplyCompanyList = new ArrayList<>();
        List<Long> demandCompanyList = new ArrayList<>();

        List<Long> productId = new ArrayList<>();
        List<Integer> productAmount = new ArrayList<>();

        // Feign 요청으로 hub Id 알아올 companyId 추출
        orderDtoList.forEach(dto -> {
            supplyCompanyList.add(dto.supplyCompanyId());
            demandCompanyList.add(dto.demandCompanyId());
            productId.add(dto.productId());
            productAmount.add(-dto.amount());
        });

        /**
         * supplyCompanyList.add(dto.supplyCompanyId());
         * demandCompanyList.add(dto.demandCompanyId());
         *
         * 위 2개의 list 를 Company 로 보내서 starthub endhub + 경유해야 할 hub 들 모두 반환
         *
         * 반환하고 hub movement Info 객체 생성을 위해 사용될 예정
         *
         */


        // 수량 줄이는 로직
        companyClient.updateProduct(userId, new ProductsUpdateQuantitiesReqDto(productId, productAmount));
        // 성공시
        // 경로 생성

        // 필요한 것들 최종 start Hub 와 최종 end Hub + 경유할 Hub : 반환값에서 HubMovementId, estimatedTime, distance, startHub, endHub, address
        Long hubMovementId = 1L;
        Double distance = 3D;
        LocalTime estimateTime = LocalTime.of(3, 3, 3);
        Long startHub = 1L;
        Long endHub = 1L;
        String address = "주소";


        // 성공시 객체 생성
        List<Order> orderList = new ArrayList<>();
        orderDtoList.forEach(dto -> orderList.add(new Order(userId, dto.productId(), dto.supplyCompanyId(), dto.demandCompanyId(), dto.amount())));

        DeliveryRoute deliveryRoute = new DeliveryRoute(hubMovementId, distance, estimateTime);

        Delivery delivery = new Delivery(deliveryRoute, DeliveryStatus.WAITING_HUB, managerId, startHub, endHub, address, userId);
        orderList.forEach(delivery::addOrder);
        deliveryRepository.save(delivery);
    }

    @Transactional
    public OrderResponseDto updateOrder(Long orderId,Long deliveryId, UpdateAmountOrderDto updateAmountOrderDto, String userId, String userRole) {
        // Master 일 때: deliveryId 와 orderId 를 통해 delivery 를 찾고 주문량 수정!
        // Manager 일 때: userId 를 통해 Auth Server 로 요청해 Manager 의 담당 Hub 를 찾아 findOrder 의 SupplyHub 와 manager 관리 허브Id 가 동일하면 수행, 아니면 X
        if (!roleCheck.CHECK_MASTER(userRole)) {
            if (roleCheck.CHECK_COMPANY(userRole) || roleCheck.CHECK_SHIPPER(userRole))
                throw new CoreApiException(ErrorType.BAD_REQUEST);
            
            else {
                /**
                 * // auth server
                 * // MANGER 로직
                 */
            }
        }
        // MASTER 로직
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        Order findOrder = delivery.getOrderList().stream().filter(order -> order.getId().equals(orderId))
                        .findAny().orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));

        Integer result = findOrder.getAmount() - updateAmountOrderDto.amount();
        List<Long> productId = new ArrayList<>();
        List<Integer> productAmount = new ArrayList<>();
        productId.add(findOrder.getProductId());
        productAmount.add(result);

        companyClient.updateProduct(userId, new ProductsUpdateQuantitiesReqDto(productId, productAmount));
        findOrder.updateAmount(updateAmountOrderDto.amount());
        return OrderResponseDto.of(findOrder);
    }


    @Transactional
    public void deleteOrder(Long orderId, Long deliveryId, String userId, String userRole) {
        if (!roleCheck.CHECK_MASTER(userRole)) {
            if (roleCheck.CHECK_COMPANY(userRole) || roleCheck.CHECK_SHIPPER(userRole))
                throw new CoreApiException(ErrorType.BAD_REQUEST);
            else {
                /**
                 * // auth server
                 * // MANGER 로직
                 */
            }
        }
        Delivery delivery = deliveryRepository.findByIdAndIsDeleteFalse(deliveryId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        Order findOrder = delivery.getOrderList().stream().filter(order -> order.getId().equals(orderId)).findAny().orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        delivery.getOrderList().remove(findOrder);
        findOrder.softDelete(userId);
    }
}
