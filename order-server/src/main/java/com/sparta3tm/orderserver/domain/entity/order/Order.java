package com.sparta3tm.orderserver.domain.entity.order;

import com.sparta3tm.common.BaseEntity;
import com.sparta3tm.orderserver.domain.entity.delivery.Delivery;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_orders")
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long supplyCompanyId;
    @Column(nullable = false)
    private Long demandCompanyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Column(nullable = false)
    private Integer amount;


    public Order(String userId, Long productId, Long supplyCompanyId, Long demandCompanyId, Integer amount) {
        this.userId = userId;
        this.productId = productId;
        this.supplyCompanyId = supplyCompanyId;
        this.demandCompanyId = demandCompanyId;
        this.amount = amount;
    }

    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void updateAmount(Integer amount) {
        this.amount = amount;
    }

}
