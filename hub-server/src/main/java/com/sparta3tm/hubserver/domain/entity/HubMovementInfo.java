package com.sparta3tm.hubserver.domain.entity;

import com.sparta3tm.common.BaseEntity;
import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "p_hub_movement_infos")
public class HubMovementInfo extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startHub;
    private Long endHub;

    private Time estimatedTime;
    private Double estimatedDistance;


}
