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
    private Long productId;

    private Long supplyCompanyId;
    private Long demandCompanyId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private Integer amount;

}
