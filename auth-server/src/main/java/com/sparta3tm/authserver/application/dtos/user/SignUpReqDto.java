package com.sparta3tm.authserver.application.dtos.user;

import com.sparta3tm.authserver.domain.user.UserRole;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpReqDto {
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "Username must be 4 to 10 characters long and contain only lowercase letters and numbers")
    private String userId;

    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$",
            message = "Password must be 8 to 15 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    private String phoneNumber;

    private UserRole role;
}
