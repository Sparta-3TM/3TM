package com.sparta3tm.authserver.domain.DM;

import com.sparta3tm.authserver.domain.user.User;
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
@Entity(name = "p_delivery_managers")
public class DeliveryManager extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "hub_id", nullable = false)
    private Long hubId;

    @Column(name = "slack_id", nullable = false)
    private String slackId;

    @Column(name = "manager_type", nullable = false)
    private ManagerType managerType;

    public void updateDeliveryManagerInfo(User user, Long hubId, String slackId, ManagerType managerType) {
        this.user = user;
        this.hubId = hubId;
        this.slackId = slackId;
        this.managerType = managerType;
    }
}
