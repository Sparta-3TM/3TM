package com.sparta3tm.orderserver.application.service;

import com.sparta3tm.common.support.RoleCheck;
import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.orderserver.application.dto.request.delivery.DeliveryUpdateDto;
import com.sparta3tm.orderserver.application.dto.response.delivery.DeliveryResponseDto;
import com.sparta3tm.orderserver.domain.entity.delivery.Delivery;
import com.sparta3tm.orderserver.domain.entity.delivery.DeliveryStatus;
import com.sparta3tm.orderserver.domain.entity.delivery_route.DeliveryRoute;
import com.sparta3tm.orderserver.domain.repository.DeliveryRepository;
import com.sparta3tm.orderserver.infrastructure.client.HubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final HubClient hubClient;
    private final RoleCheck roleCheck = new RoleCheck();

    @Transactional(readOnly = true)
    public DeliveryResponseDto searchDeliveryById(Long deliveryId, String userId, String userRole) {
        Delivery delivery = deliveryRepository.findByIdAndIsDeleteFalse(deliveryId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        if (roleCheck.CHECK_SHIPPER(userRole))
            if (!delivery.getShipperId().equals(userId)) throw new CoreApiException(ErrorType.BAD_REQUEST);

        return DeliveryResponseDto.of(delivery);
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponseDto> searchDeliveryList(Pageable pageable, String userId, String userRole) {
        // 모든 배송 리스트 조회는 MASTER 와 MANAGER 만 가능
        if (roleCheck.CHECK_MASTER(userRole) && roleCheck.CHECK_MANAGER(userRole)) throw new CoreApiException(ErrorType.BAD_REQUEST);
        return deliveryRepository.findAllByIsDeleteFalse(pageable)
                .stream()
                .map(DeliveryResponseDto::of)
                .toList();
    }

    @Transactional
    public DeliveryResponseDto updateDelivery(Long deliveryId, DeliveryUpdateDto deliveryUpdateDto, String userId, String userRole) {
        Delivery delivery = deliveryRepository.findByIdAndIsDeleteFalse(deliveryId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        DeliveryRoute route = delivery.getDeliveryRoute();
        if (roleCheck.CHECK_SHIPPER(userRole))
            if (!delivery.getShipperId().equals(userId))
                throw new CoreApiException(ErrorType.BAD_REQUEST);
        else if (roleCheck.CHECK_COMPANY(userRole)) {
            throw new CoreApiException(ErrorType.BAD_REQUEST);
        }

        // 와 생각해보니 Hub Movement info 변경될 때도, Delivery 건들어 줘야함;;
        hubClient.removeStartHub(route.getHmiId(), userId);

        delivery.updateAddress(deliveryUpdateDto.address());
        delivery.updateRecipientSlack(deliveryUpdateDto.recipientSlack());
        delivery.updateDeliveryStatus(DeliveryStatus.fromString(deliveryUpdateDto.deliveryStatus()));

        return DeliveryResponseDto.of(delivery);
    }
}
