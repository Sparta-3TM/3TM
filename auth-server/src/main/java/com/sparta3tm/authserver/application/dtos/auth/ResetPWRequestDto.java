package com.sparta3tm.authserver.application.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResetPWRequestDto {
    private String userId;
    private String resetCode;
    private String newPassword;
}
