package com.sparta3tm.authserver.application.dtos.user;

import com.sparta3tm.authserver.domain.user.User;
import com.sparta3tm.authserver.domain.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResDto {
    private Long id;
    private String userId;
    private String userName;
    private String phoneNumber;
    private UserRole userRole;

    public static UserResDto from(User user) {
        return UserResDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .userRole(user.getUserRole())
                .build();
    }
}
