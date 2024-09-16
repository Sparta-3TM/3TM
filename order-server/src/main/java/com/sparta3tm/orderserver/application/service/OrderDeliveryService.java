package com.sparta3tm.orderserver.application.service;

import com.sparta3tm.common.support.RoleCheck;
import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.orderserver.application.dto.request.order.OrderListRequestDto;
import com.sparta3tm.orderserver.application.dto.request.order.OrderRequestDto;
import com.sparta3tm.orderserver.application.dto.request.order.UpdateAmountOrderDto;
import com.sparta3tm.orderserver.application.dto.response.order.OrderResponseDto;
import com.sparta3tm.orderserver.domain.entity.delivery.Delivery;
import com.sparta3tm.orderserver.domain.entity.delivery.DeliveryStatus;
import com.sparta3tm.orderserver.domain.entity.delivery_route.DeliveryRoute;
import com.sparta3tm.orderserver.domain.entity.order.Order;
import com.sparta3tm.orderserver.domain.repository.DeliveryRepository;
import com.sparta3tm.orderserver.domain.repository.OrderRepository;
import com.sparta3tm.orderserver.infrastructure.client.CompanyClient;
import com.sparta3tm.orderserver.infrastructure.client.HubClient;
import com.sparta3tm.orderserver.infrastructure.client.dto.company.CompaniesInfosReqDto;
import com.sparta3tm.orderserver.infrastructure.client.dto.company.CompaniesInfosResDto;
import com.sparta3tm.orderserver.infrastructure.client.dto.company.ProductsUpdateQuantitiesReqDto;
import com.sparta3tm.orderserver.infrastructure.client.dto.hub.RequestHMIDto;
import com.sparta3tm.orderserver.infrastructure.client.dto.hub.ResponseHMIDto;
import com.sparta3tm.orderserver.infrastructure.client.dto.hub.ResponseHubManagerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final CompanyClient companyClient;
    private final HubClient hubClient;
    private final RoleCheck roleCheck = new RoleCheck();

    @Transactional
    public void createOrder(OrderListRequestDto orderRequestDto, String userId, String userRole) {
        List<OrderRequestDto> orderDtoList = orderRequestDto.orderDtoList();
        Long shipperId = orderRequestDto.shipperId();
        List<Long> supplyCompanyList = new ArrayList<>();
        List<Long> demandCompanyList = new ArrayList<>();
        List<Long> productId = new ArrayList<>();
        List<Integer> productAmount = new ArrayList<>();
        orderDtoList.forEach(dto -> {
            supplyCompanyList.add(dto.supplyCompanyId());
            demandCompanyList.add(dto.demandCompanyId());
            productId.add(dto.productId());
            productAmount.add(-dto.amount());
        });

        CompaniesInfosResDto companyDto = (CompaniesInfosResDto) companyClient.getCompanyById(userId, new CompaniesInfosReqDto(supplyCompanyList, demandCompanyList)).getData();

        List<Long> supplyList = companyDto.getSupplyHubIds();
        List<Long> demandList = companyDto.getDemandHubIds();
        Set<Long> combinedSet = new LinkedHashSet<>(supplyList);
        combinedSet.addAll(demandList);

        List<Long> commonList = new ArrayList<>(combinedSet);
        commonList.remove(supplyList.getFirst());
        commonList.remove(demandList.getFirst());

        ResponseHMIDto data = (ResponseHMIDto) hubClient.createHmi(new RequestHMIDto(supplyList.getFirst(), demandList.getFirst(), commonList), userId, userRole).getData();

        companyClient.updateProduct(userId, new ProductsUpdateQuantitiesReqDto(productId, productAmount));
        Long hubMovementId = data.id();
        Double distance = data.estimatedDistance();
        LocalTime estimateTime = data.estimatedTime();
        Long startHub = data.startHub();
        Long endHub = data.endHub();
        String address = data.address();

        List<Order> orderList = new ArrayList<>();
        orderDtoList.forEach(dto -> orderList.add(new Order(userId, dto.productId(), dto.supplyCompanyId(), dto.demandCompanyId(), dto.amount())));
        DeliveryRoute deliveryRoute = new DeliveryRoute(hubMovementId, distance, estimateTime);
        Delivery delivery = new Delivery(deliveryRoute, DeliveryStatus.WAITING_HUB, shipperId, startHub, endHub, address, userId);
        orderList.forEach(delivery::addOrder);
        deliveryRepository.save(delivery);
    }

    @Transactional
    public OrderResponseDto updateOrder(Long orderId, Long deliveryId, UpdateAmountOrderDto updateAmountOrderDto, String userId, String userRole) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        DeliveryRoute route = delivery.getDeliveryRoute();

        if (roleCheck.CHECK_SHIPPER(userRole)) {
            if (!delivery.getShipperId().equals(userId)) {
                throw new CoreApiException(ErrorType.BAD_REQUEST);
            }
        } else if (roleCheck.CHECK_MANAGER(userRole)) {
            List<ResponseHubManagerDto> dataList = (List<ResponseHubManagerDto>) hubClient.searchHmiManager(route.getHmiId(), userId, userRole).getData();
            boolean isMatched = false;
            for (ResponseHubManagerDto data : dataList) {
                if (data.managerId().equals(userId)) {
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) throw new CoreApiException(ErrorType.BAD_REQUEST);
        } else if (roleCheck.CHECK_COMPANY(userRole)) throw new CoreApiException(ErrorType.BAD_REQUEST);

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
        Delivery delivery = deliveryRepository.findByIdAndIsDeleteFalse(deliveryId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        DeliveryRoute route = delivery.getDeliveryRoute();

        if (roleCheck.CHECK_COMPANY(userRole) || roleCheck.CHECK_SHIPPER(userRole)) {
            throw new CoreApiException(ErrorType.BAD_REQUEST);
        } else if (roleCheck.CHECK_MANAGER(userRole)) {
            List<ResponseHubManagerDto> dataList = (List<ResponseHubManagerDto>) hubClient.searchHmiManager(route.getHmiId(), userId, userRole).getData();
            boolean isMatched = false;
            for (ResponseHubManagerDto data : dataList) {
                if (data.managerId().equals(userId)) {
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) throw new CoreApiException(ErrorType.BAD_REQUEST);
        }

        Order findOrder = delivery.getOrderList().stream().filter(order -> order.getId().equals(orderId)).findAny().orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        delivery.getOrderList().remove(findOrder);
        findOrder.softDelete(userId);
    }
}
