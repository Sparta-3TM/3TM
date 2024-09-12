package com.sparta3tm.orderserver.domain.entity.delivery_route;

import com.sparta3tm.common.BaseEntity;
import com.sparta3tm.orderserver.domain.entity.delivery.Delivery;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_delivery_routes")
public class DeliveryRoute extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "deliveryRoute", fetch = FetchType.LAZY)
    private Delivery delivery;

    @Column(nullable = false)
    private Long hmiId;
    @Column(nullable = false)
    private Double distance;
    @Column(nullable = false)
    private LocalTime duration_time;

    public DeliveryRoute(Long hmiId, Double distance, LocalTime duration_time) {
        this.hmiId = hmiId;
        this.distance = distance;
        this.duration_time = duration_time;
    }

    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
