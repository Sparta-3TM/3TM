package com.sparta3tm.hubserver.domain.entity;

import com.sparta3tm.common.BaseEntity;
import com.sparta3tm.hubserver.application.dto.RequestHubDto;
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

    private String name;
    private String address;
    private Double latitude;
    private Double longitude;


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
}
