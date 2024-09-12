package com.sparta3tm.authserver.controller;

import com.sparta3tm.authserver.application.AuthService;
import com.sparta3tm.authserver.application.dtos.user.SignInReqDto;
import com.sparta3tm.authserver.application.dtos.user.SignUpReqDto;
import com.sparta3tm.authserver.config.JwtTokenProvider;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.common.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입 API
    @PostMapping("/signUp")
    public ApiResponse<?> registerUser(@RequestBody SignUpReqDto signUpReqDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ApiResponse.error(ErrorType.INVALID);
        }
        authService.registerUser(signUpReqDto);
        return ApiResponse.success();
    }

    // 로그인 API
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody SignInReqDto signInReqDto) {
        String token = authService.login(signInReqDto.getUserId(), signInReqDto.getPassword());
        return ApiResponse.success(token);  // JWT 토큰 반환
    }

    // 토큰 검증 API
    @PostMapping("/validate-token")
    public boolean validateToken(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);  // "Bearer " 부분 제거

        return jwtTokenProvider.validateToken(token);
    }


}
