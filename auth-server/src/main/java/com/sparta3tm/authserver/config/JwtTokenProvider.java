package com.sparta3tm.authserver.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    // JWT 토큰 생성
    public String generateToken(String userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("X-USER-ID", userId);
        claims.put("X-USER-ROLE", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())  // userId를 subject로 설정
                .setIssuedAt(new Date())  // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))  // 만료 시간
                .signWith(SignatureAlgorithm.HS512, jwtSecret)  // 서명 알고리즘과 비밀키 설정
                .compact();
    }

    public boolean validateToken(String token) {
        System.out.println("토큰 검증!!!!!!!!!!!!!");
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 클레임(Claims)을 추출하는 메소드
    private Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();

        System.out.println("클레임 추출!!!!!!!!!!!!!");

        return claims;

    }

    // 토큰에서 User ID 추출
    public String getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        System.out.println("ID 추출!!!!!!!!!!!!!");
        return claims.get("X-USER-ID", String.class);  // 클레임에서 'X-USER-ID' 추출
    }

    // 토큰에서 User Role 추출
    public String getUserRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        System.out.println("Role 추출!!!!!!!!!!!!!");
        return claims.get("X-USER-ROLE", String.class);  // 클레임에서 'X-USER-ROLE' 추출
    }
}
