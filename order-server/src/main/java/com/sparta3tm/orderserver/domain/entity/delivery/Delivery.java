package com.sparta3tm.orderserver.domain.entity.delivery;

import com.sparta3tm.common.BaseEntity;
import com.sparta3tm.orderserver.domain.entity.delivery_route.DeliveryRoute;
import com.sparta3tm.orderserver.domain.entity.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_deliveries")
public class Delivery extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private List<Order> orderList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_route_id", nullable = false)
    private DeliveryRoute deliveryRoute;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(nullable = false)
    private Long shipperId;
    @Column(nullable = false)
    private Long startHub;
    @Column(nullable = false)
    private Long endHub;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String recipientSlack;

    public Delivery(DeliveryRoute deliveryRoute, DeliveryStatus deliveryStatus,Long managerId, Long startHub, Long endHub, String address, String recipientSlack) {
        this.deliveryRoute = deliveryRoute;
        deliveryRoute.addDelivery(this);
        this.shipperId = managerId;
        this.deliveryStatus = deliveryStatus;
        this.startHub = startHub;
        this.endHub = endHub;
        this.address = address;
        this.recipientSlack = recipientSlack;
    }

    public void addOrder(Order order) {
        this.orderList.add(order);
        order.addDelivery(this);
    }

    public void updateDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public void updateRecipientSlack(String recipientSlack) {
        this.recipientSlack = recipientSlack;
    }
}
