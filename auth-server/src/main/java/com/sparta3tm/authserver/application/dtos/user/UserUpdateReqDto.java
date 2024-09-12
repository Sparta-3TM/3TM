package com.sparta3tm.authserver.application.dtos.user;

import com.sparta3tm.authserver.domain.user.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateReqDto {
    @NotNull
    private String username;
    @NotNull
    private String phoneNumber;
    @NotNull
    private UserRole role;
}
