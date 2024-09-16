package com.sparta3tm.hubserver.domain.entity;

import com.sparta3tm.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "p_hub_movement_infos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class HubMovementInfo extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startHub;
    private Long endHub;

    private String address;

    private LocalTime duration;
    private Double distance;

    @OneToMany(mappedBy = "parentMovementInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("index ASC")
    private List<HubMovementInfo> subMovementInfo = new ArrayList<>();
    private Integer index;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @Setter
    private HubMovementInfo parentMovementInfo;

    public HubMovementInfo(Long startHub, Long endHub, LocalTime duration, Double distance) {
        this.startHub = startHub;
        this.endHub = endHub;
        this.duration = duration;
        this.distance = distance;
    }

    public void updateStartHub(Long startHub) {
        this.startHub = startHub;
    }

    public void updateEndHub(Long endHub) {
        this.endHub = endHub;
    }

    public void addSubMovement(HubMovementInfo subMovement) {
        subMovement.setParentMovementInfo(this);
        subMovementInfo.add(subMovement);
    }

    public void removeSubMovement(HubMovementInfo subMovement) {
        subMovementInfo.remove(subMovement);
    }

    public void addIndex(int index) {
        this.index = index;
    }

    public void updateAddress(String address) {
        this.address = address;
    }


    public void updateDuration(LocalTime duration) {
        this.duration = duration;
    }

    public void updateDistance(Double distance) {
        this.distance = distance;
    }
}
