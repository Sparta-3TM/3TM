package com.sparta3tm.authserver.application;

import com.sparta3tm.authserver.application.dtos.user.SignUpReqDto;
import com.sparta3tm.authserver.config.JwtTokenProvider;
import com.sparta3tm.authserver.domain.user.User;
import com.sparta3tm.authserver.domain.user.UserRepository;
import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    // 회원가입 로직
    public void registerUser(SignUpReqDto signUpReqDto) {
        // 사용자 이름 중복 확인
        if (userRepository.findByUserId(signUpReqDto.getUserId()).isPresent()) {
            throw new CoreApiException(ErrorType.CONFLICT);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpReqDto.getPassword());

        // 새로운 사용자 저장
        User newUser = new User(signUpReqDto.getUserId(),
                signUpReqDto.getUsername(),
                encodedPassword,
                signUpReqDto.getPhoneNumber(),
                signUpReqDto.getRole());

        userRepository.save(newUser);
    }

    // 로그인 로직
    public String login(String userId, String password) {
        // 사용자 조회
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isEmpty()) {
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }

        User user = userOptional.get();

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getUserRole().name());

        redisTemplate.opsForValue().set(token, token);

        // JWT 토큰 생성
        return token;
    }
}
