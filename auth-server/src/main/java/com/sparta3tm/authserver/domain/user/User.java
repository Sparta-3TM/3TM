package com.sparta3tm.authserver.domain.user;

import com.sparta3tm.authserver.domain.DM.DeliveryManager;
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
@Entity(name = "p_users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @OneToOne(mappedBy = "user")
    private DeliveryManager deliveryManager;

    public User(String userId, String userName, String password, String phoneNumber, UserRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }

    public void updateUserInfo(String userName, String phoneNumber, UserRole userRole) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }
}
