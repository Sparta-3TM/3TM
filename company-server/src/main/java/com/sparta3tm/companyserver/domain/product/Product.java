package com.sparta3tm.companyserver.domain.product;

import com.sparta3tm.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_delete is false")
@Entity(name = "p_products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "hub_id", nullable = false)
    private Long hubId;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    public void updateProductInfo(Long companyId, Long hubId, String productName, Integer quantity) {
        this.companyId = companyId;
        this.hubId = hubId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public void controlQuantity(Integer quantity) {
        this.quantity += quantity; // 수량 조절 + or -
    }
}
