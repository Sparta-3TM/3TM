package com.sparta3tm.authserver.application.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInReqDto {
    private String userId;
    private String password;
}
