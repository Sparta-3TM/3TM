package com.sparta3tm.orderserver.domain.entity.delivery;

import com.sparta3tm.common.BaseEntity;
import com.sparta3tm.orderserver.domain.entity.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_deliveries")
public class Delivery extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;
    private DeliveryStatus deliveryStatus;

    private Long startHub;
    private Long endHub;
    private String address;

    private Long recipient;
    private String recipientSlack;

}
