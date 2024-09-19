package com.sparta3tm.authserver.controller;

import com.sparta3tm.authserver.application.AuthService;
import com.sparta3tm.authserver.application.dtos.auth.AuthResponseDto;
import com.sparta3tm.authserver.application.dtos.auth.ResetPWRequestDto;
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

    @PostMapping("/resetPassword")
    public ApiResponse<?> resetPassword(@RequestBody ResetPWRequestDto resetPWRequestDto) {

        try {
            String response = authService.resetPassword(resetPWRequestDto.getUserId(),resetPWRequestDto.getResetCode(),resetPWRequestDto.getNewPassword());
            return ApiResponse.success(response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(ErrorType.INVALID);
        }
    }

    @PostMapping("/validate-token")
    public AuthResponseDto validateToken(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);  // "Bearer " 부분 제거

        boolean isValid = jwtTokenProvider.validateToken(token);
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        String userRole = jwtTokenProvider.getUserRoleFromToken(token);

        // 검증 결과와 함께 AuthResponse 반환
        return new AuthResponseDto(isValid, userId, userRole);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }


}
