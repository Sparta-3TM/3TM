package com.sparta3tm.hubserver.domain.entity;

import com.sparta3tm.common.BaseEntity;
import com.sparta3tm.hubserver.application.dto.hub.RequestHubDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_hubs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Hub extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;

    private String managerId;

    public Hub(String name, String address, Double latitude, Double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Hub(RequestHubDto requestHubDto) {
        this.name = requestHubDto.hubName();
        this.address = requestHubDto.address();
        this.latitude = requestHubDto.latitude();
        this.longitude = requestHubDto.longitude();
    }

    public Hub update(RequestHubDto requestHubDto) {
        this.name = requestHubDto.hubName();
        this.address = requestHubDto.address();
        this.latitude = requestHubDto.latitude();
        this.longitude = requestHubDto.longitude();
        return this;
    }

    public Hub updateManager(String userId) {
        managerId = userId;
        return this;
    }
}
